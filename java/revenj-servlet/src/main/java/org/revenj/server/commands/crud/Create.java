package org.revenj.server.commands.crud;

import org.revenj.patterns.*;
import org.revenj.server.CommandResult;
import org.revenj.server.ServerCommand;

import java.io.IOException;
import java.util.Optional;

public final class Create implements ServerCommand {

	private final DomainModel domainModel;

	public Create(DomainModel domainModel) {
		this.domainModel = domainModel;
	}

	public static final class Argument<TFormat> {
		public String Name;
		public TFormat Data;

		public Argument(String name, TFormat data) {
			this.Name = name;
			this.Data = data;
		}

		@SuppressWarnings("unused")
		private Argument() {
		}
	}

	@Override
	public <TInput, TOutput> CommandResult<TOutput> execute(ServiceLocator locator, Serialization<TInput> input, Serialization<TOutput> output, TInput data) {
		Argument<TInput> arg;
		try {
			arg = input.deserialize(data, Argument.class, data.getClass());
		} catch (IOException e) {
			return CommandResult.badRequest(e.getMessage());
		}
		Optional<Class<?>> manifest = domainModel.find(arg.Name);
		if (!manifest.isPresent()) {
			return CommandResult.badRequest("Unable to find specified domain object: " + arg.Name);
		}
		if (arg.Data == null) {
			return CommandResult.badRequest("Data to create not specified.");
		}
		AggregateRoot instance;
		try {
			instance = (AggregateRoot) input.deserialize(manifest.get(), arg.Data);
		} catch (IOException e) {
			return CommandResult.badRequest("Error deserializing provided input for: " + arg.Name + ". Reason: " + e.getMessage());
		}
		PersistableRepository repository;
		try {
			repository = locator.resolve(PersistableRepository.class, manifest.get());
		} catch (ReflectiveOperationException e) {
			return CommandResult.badRequest("Error resolving repository for: " + arg.Name + ". Reason: " + e.getMessage());
		}
		try {
			String uri = repository.insert(instance);
			return new CommandResult<>(output.serialize(uri), "Object created", 201);
		} catch (IOException e) {
			return CommandResult.badRequest(e.getMessage());
		}
	}
}