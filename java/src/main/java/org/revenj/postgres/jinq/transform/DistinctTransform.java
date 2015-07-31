package org.revenj.postgres.jinq.transform;

import org.revenj.postgres.jinq.jpqlquery.JPQLQuery;
import org.revenj.postgres.jinq.jpqlquery.SelectOnly;

public class DistinctTransform extends JPQLNoLambdaQueryTransform {
    public DistinctTransform(JPQLQueryTransformConfiguration config) {
        super(config);
    }

    public <U, V> JPQLQuery<U> apply(JPQLQuery<V> query, org.revenj.postgres.jinq.transform.SymbExArgumentHandler parentArgumentScope) throws QueryTransformException {
        if (query.canDistinct()) {
            SelectOnly<V> select = (SelectOnly<V>) query;

            // Create the new query, merging in the analysis of the method
            SelectOnly<U> toReturn = (SelectOnly<U>) select.shallowCopy();
            toReturn.isDistinct = true;

            return toReturn;
        }
        throw new QueryTransformException("Existing query cannot be transformed further");
    }

    @Override
    public String getTransformationTypeCachingTag() {
        return DistinctTransform.class.getName();
    }

}
