package org.revenj.postgres.jinq.transform;

import org.revenj.postgres.jinq.jpqlquery.JPQLQuery;

public abstract class JPQLOneLambdaQueryTransform extends JPQLQueryTransform {

    JPQLOneLambdaQueryTransform(JPQLQueryTransformConfiguration config) {
        super(config);
    }

    public <U, V> JPQLQuery<U> apply(JPQLQuery<V> query, LambdaAnalysis lambda, SymbExArgumentHandler parentArgumentScope) throws QueryTransformException {
        return null;
    }
}
