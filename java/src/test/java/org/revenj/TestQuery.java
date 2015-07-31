package org.revenj;

import gen.model.Boot;
import gen.model.Seq.Next;
import gen.model.Seq.repositories.NextRepository;
import gen.model.test.*;
import gen.model.test.repositories.*;
import org.jinq.orm.stream.JinqStream;
import org.junit.Assert;
import org.junit.Test;
import org.revenj.patterns.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestQuery {

    @Test
    public void simpleQuery() throws IOException, SQLException {
        ServiceLocator locator = Boot.configure("jdbc:postgresql://localhost:5432/revenj");
        NextRepository repository = locator.resolve(NextRepository.class);
        repository.insert(new Next());
        List<Next> search = repository.search();
        Query<Next> stream = repository.query();
        List<Next> list = stream.list();
        Assert.assertEquals(search.size(), list.size());
    }

    @Test
    public void queryWithFilter() throws IOException, SQLException {
        ServiceLocator locator = Boot.configure("jdbc:postgresql://localhost:5432/revenj");
        NextRepository repository = locator.resolve(NextRepository.class);
        String uri = repository.insert(new Next());
        int id = Integer.parseInt(uri);
        Optional<Next> found = repository.query().filter(next -> next.getID() == id).findAny();
        Assert.assertTrue(found.isPresent());
        Assert.assertEquals(id, found.get().getID());
    }

    @Test
    public void queryWithFilterAndCount() throws IOException, SQLException {
        ServiceLocator locator = Boot.configure("jdbc:postgresql://localhost:5432/revenj");
        NextRepository repository = locator.resolve(NextRepository.class);
        String uri = repository.insert(new Next());
        int id = Integer.parseInt(uri);
        long found = repository.query().filter(next -> next.getID() == id).count();
        Assert.assertEquals(1, found);
        //TODO: doesn't work correctly
        //found = repository.query().filter(next -> false).count();
        //Assert.assertEquals(0, found);
    }

    @Test
    public void queryWithFilterAndFindAny() throws IOException, SQLException {
        ServiceLocator locator = Boot.configure("jdbc:postgresql://localhost:5432/revenj");
        NextRepository repository = locator.resolve(NextRepository.class);
        String uri = repository.insert(new Next());
        int id = Integer.parseInt(uri);
        repository.insert(new Next());
        boolean found = repository.query().anyMatch(next -> next.getID() == id);
        Assert.assertTrue(found);
        found = repository.query().allMatch(next -> next.getID() == id);
        Assert.assertFalse(found);
        found = repository.query().noneMatch(next -> next.getID() == id);
        Assert.assertFalse(found);
        found = repository.query().noneMatch(next -> next.getID() == id + 2);
        Assert.assertTrue(found);
    }
}
