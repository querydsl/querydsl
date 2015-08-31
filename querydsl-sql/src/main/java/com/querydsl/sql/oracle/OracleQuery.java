/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
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
package com.querydsl.sql.oracle;

import java.sql.Connection;

import com.querydsl.core.DefaultQueryMetadata;
import com.querydsl.core.QueryFlag.Position;
import com.querydsl.core.QueryMetadata;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Predicate;
import com.querydsl.sql.AbstractSQLQuery;
import com.querydsl.sql.Configuration;
import com.querydsl.sql.OracleTemplates;
import com.querydsl.sql.SQLTemplates;

/**
 * {@code OracleQuery} provides Oracle specific extensions to the base SQL query type
 *
 * @author tiwe
 * @param <T> result type
 */
public class OracleQuery<T> extends AbstractSQLQuery<T, OracleQuery<T>> {

    private static final String CONNECT_BY = "\nconnect by ";

    private static final String CONNECT_BY_NOCYCLE_PRIOR = "\nconnect by nocycle prior ";

    private static final String CONNECT_BY_PRIOR = "\nconnect by prior ";

    private static final String ORDER_SIBLINGS_BY = "\norder siblings by ";

    private static final String START_WITH = "\nstart with ";

    public OracleQuery(Connection conn) {
        this(conn, OracleTemplates.DEFAULT, new DefaultQueryMetadata());
    }

    public OracleQuery(Connection conn, SQLTemplates templates) {
        this(conn, templates, new DefaultQueryMetadata());
    }

    public OracleQuery(Connection conn, Configuration configuration) {
        super(conn, configuration, new DefaultQueryMetadata());
    }

    public OracleQuery(Connection conn, Configuration configuration, QueryMetadata metadata) {
        super(conn, configuration, metadata);
    }

    protected OracleQuery(Connection conn, SQLTemplates templates, QueryMetadata metadata) {
        super(conn, new Configuration(templates), metadata);
    }

    /**
     * CONNECT BY specifies the relationship between parent rows and child rows of the hierarchy.
     *
     * @param cond condition
     * @return the current object
     */
    public OracleQuery<T> connectByPrior(Predicate cond) {
        return addFlag(Position.BEFORE_ORDER, CONNECT_BY_PRIOR, cond);
    }

    /**
     * CONNECT BY specifies the relationship between parent rows and child rows of the hierarchy.
     *
     * @param cond condition
     * @return the current object
     */
    public OracleQuery<T> connectBy(Predicate cond) {
        return addFlag(Position.BEFORE_ORDER, CONNECT_BY, cond);
    }

    /**
     * CONNECT BY specifies the relationship between parent rows and child rows of the hierarchy.
     *
     * @param cond condition
     * @return the current object
     */
    public OracleQuery<T> connectByNocyclePrior(Predicate cond) {
        return addFlag(Position.BEFORE_ORDER, CONNECT_BY_NOCYCLE_PRIOR, cond);
    }

    /**
     * START WITH specifies the root row(s) of the hierarchy.
     *
     * @param cond condition
     * @return the current object
     */
    public <A> OracleQuery<T> startWith(Predicate cond) {
        return addFlag(Position.BEFORE_ORDER, START_WITH, cond);
    }

    /**
     * ORDER SIBLINGS BY preserves any ordering specified in the hierarchical query clause and then
     * applies the order_by_clause to the siblings of the hierarchy.
     *
     * @param path path
     * @return the current object
     */
    public OracleQuery<T> orderSiblingsBy(Expression<?> path) {
        return addFlag(Position.BEFORE_ORDER, ORDER_SIBLINGS_BY, path);
    }

    @Override
    public OracleQuery<T> clone(Connection conn) {
        OracleQuery<T> q = new OracleQuery<T>(conn, getConfiguration(), getMetadata().clone());
        q.clone(this);
        return q;
    }

    // TODO : connect by root

    // TODO : connect by iscycle

    // TODO : connect by isleaf (pseudocolumn)

    // TODO : sys connect path

    @SuppressWarnings("unchecked")
    @Override
    public <U> OracleQuery<U> select(Expression<U> expr) {
        queryMixin.setProjection(expr);
        return (OracleQuery<U>) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public OracleQuery<Tuple> select(Expression<?>... exprs) {
        queryMixin.setProjection(exprs);
        return (OracleQuery<Tuple>) this;
    }
}


