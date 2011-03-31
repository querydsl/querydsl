package com.mysema.query.sql;

import com.mysema.query.sql.dml.SQLDeleteClause;
import com.mysema.query.sql.dml.SQLInsertClause;
import com.mysema.query.sql.dml.SQLMergeClause;
import com.mysema.query.sql.dml.SQLUpdateClause;
import com.mysema.query.types.Expression;

/**
 * Factory interface for query and clause creation
 *
 * @author tiwe
 *
 * @param <Q> query type
 * @param <D> delete clause type
 * @param <U> update clause type
 * @param <I> insert clause type
 * @param <M> merge clause type
 */
public interface SQLQueryFactory<Q extends AbstractSQLQuery<?>,
    SQ extends AbstractSQLSubQuery<?>,
    D extends SQLDeleteClause,
    U extends SQLUpdateClause,
    I extends SQLInsertClause,
    M extends SQLMergeClause> {

    D delete(RelationalPath<?> path);

    Q from(Expression<?> from);

    I insert(RelationalPath<?> path);

    M merge(RelationalPath<?> path);

    U update(RelationalPath<?> path);

    Q query();

    SQ subQuery();

    SQ subQuery(Expression<?> from);

}
