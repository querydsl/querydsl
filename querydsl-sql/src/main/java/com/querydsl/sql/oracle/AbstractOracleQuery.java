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

import javax.inject.Provider;

import com.infradna.tool.bridge_method_injector.WithBridgeMethods;
import com.querydsl.core.QueryFlag.Position;
import com.querydsl.core.QueryMetadata;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Predicate;
import com.querydsl.sql.AbstractSQLQuery;
import com.querydsl.sql.Configuration;

/**
 * {@code OracleQuery} provides Oracle specific extensions to the base SQL query type
 *
 * @author tiwe
 * @param <T> result type
 * @param <C> the concrete subtype
 */
public abstract class AbstractOracleQuery<T, C extends AbstractOracleQuery<T, C>> extends AbstractSQLQuery<T, C> {

    protected static final String CONNECT_BY = "\nconnect by ";

    protected static final String CONNECT_BY_NOCYCLE_PRIOR = "\nconnect by nocycle prior ";

    protected static final String CONNECT_BY_PRIOR = "\nconnect by prior ";

    protected static final String ORDER_SIBLINGS_BY = "\norder siblings by ";

    protected static final String START_WITH = "\nstart with ";

    public AbstractOracleQuery(Connection conn, Configuration configuration, QueryMetadata metadata) {
        super(conn, configuration, metadata);
    }

    public AbstractOracleQuery(Provider<Connection> connProvider, Configuration configuration, QueryMetadata metadata) {
        super(connProvider, configuration, metadata);
    }

    /**
     * CONNECT BY specifies the relationship between parent rows and child rows of the hierarchy.
     *
     * @param cond condition
     * @return the current object
     */
    @WithBridgeMethods(OracleQuery.class)
    public C connectByPrior(Predicate cond) {
        return addFlag(Position.BEFORE_ORDER, CONNECT_BY_PRIOR, cond);
    }

    /**
     * CONNECT BY specifies the relationship between parent rows and child rows of the hierarchy.
     *
     * @param cond condition
     * @return the current object
     */
    @WithBridgeMethods(OracleQuery.class)
    public C connectBy(Predicate cond) {
        return addFlag(Position.BEFORE_ORDER, CONNECT_BY, cond);
    }

    /**
     * CONNECT BY specifies the relationship between parent rows and child rows of the hierarchy.
     *
     * @param cond condition
     * @return the current object
     */
    @WithBridgeMethods(OracleQuery.class)
    public C connectByNocyclePrior(Predicate cond) {
        return addFlag(Position.BEFORE_ORDER, CONNECT_BY_NOCYCLE_PRIOR, cond);
    }

    /**
     * START WITH specifies the root row(s) of the hierarchy.
     *
     * @param cond condition
     * @return the current object
     */
    @WithBridgeMethods(OracleQuery.class)
    public <A> C startWith(Predicate cond) {
        return addFlag(Position.BEFORE_ORDER, START_WITH, cond);
    }

    /**
     * ORDER SIBLINGS BY preserves any ordering specified in the hierarchical query clause and then
     * applies the order_by_clause to the siblings of the hierarchy.
     *
     * @param path path
     * @return the current object
     */
    @WithBridgeMethods(OracleQuery.class)
    public C orderSiblingsBy(Expression<?> path) {
        return addFlag(Position.BEFORE_ORDER, ORDER_SIBLINGS_BY, path);
    }

    // TODO : connect by root

    // TODO : connect by iscycle

    // TODO : connect by isleaf (pseudocolumn)

    // TODO : sys connect path
}


