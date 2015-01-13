/*
 * Copyright 2011, Mysema Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.sql;

import com.querydsl.core.QueryFactory;
import com.querydsl.sql.dml.SQLDeleteClause;
import com.querydsl.sql.dml.SQLInsertClause;
import com.querydsl.sql.dml.SQLMergeClause;
import com.querydsl.sql.dml.SQLUpdateClause;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.SubQueryExpression;

/**
 * Factory interface for querydsl and clause creation.
 *
 * <p>The default implementation is {@link SQLQueryFactory} and should be used for general
 * querydsl creation. Type specific variants are available if database specific queries need to be created.</p>
 *
 * @author tiwe
 *
 * @param <Q> querydsl type
 * @param <SQ> subquery type
 * @param <D> delete clause type
 * @param <U> update clause type
 * @param <I> insert clause type
 * @param <M> merge clause type
 */
public interface SQLCommonQueryFactory<Q extends SQLCommonQuery<?>, // extends AbstractSQLQuery<?>
    SQ extends AbstractSQLSubQuery<?>,
    D extends SQLDeleteClause,
    U extends SQLUpdateClause,
    I extends SQLInsertClause,
    M extends SQLMergeClause> extends QueryFactory<Q,SQ> {

    /**
     * Create a new DELETE clause
     *
     * @param path
     * @return
     */
    D delete(RelationalPath<?> path);

    /**
     * Create a new SELECT querydsl
     *
     * @param from
     * @return
     */
    Q from(Expression<?> from);

    /**
     * Create a new SELECT querydsl
     *
     * @param from
     * @return
     */
    Q from(Expression<?>... from);

    /**
     * Create a new SELECT querydsl
     *
     * @param subQuery
     * @param alias
     * @return
     */
    Q from(SubQueryExpression<?> subQuery, Path<?> alias);

    /**
     * Create a new INSERT INTO clause
     *
     * @param path
     * @return
     */
    I insert(RelationalPath<?> path);

    /**
     * Create a new MERGE clause
     *
     * @param path
     * @return
     */
    M merge(RelationalPath<?> path);

    /**
     * Create a new UPDATE clause
     *
     * @param path
     * @return
     */
    U update(RelationalPath<?> path);

    /* (non-Javadoc)
     * @see com.querydsl.QueryFactory#querydsl()
     */
    @Override
    Q query();

    /* (non-Javadoc)
     * @see com.querydsl.QueryFactory#subQuery()
     */
    @Override
    SQ subQuery();

    /**
     * @param from
     * @return
     */
    SQ subQuery(Expression<?> from);

}
