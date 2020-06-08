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
package com.querydsl.r2dbc;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.SubQueryExpression;
import com.querydsl.r2dbc.dml.R2DBCDeleteClause;
import com.querydsl.r2dbc.dml.R2DBCInsertClause;
import com.querydsl.r2dbc.dml.R2DBCUpdateClause;
import com.querydsl.sql.RelationalPath;
import io.r2dbc.spi.Connection;
import reactor.core.publisher.Mono;


/**
 * {@code AbstractSQLQueryFactory} is the base class for {@link R2DBCCommonQueryFactory} implementations
 *
 * @param <Q> query type
 * @author tiwe
 */
public abstract class AbstractSQLQueryFactory<Q extends R2DBCCommonQuery<?>> implements R2DBCCommonQueryFactory<Q,
        R2DBCDeleteClause, R2DBCUpdateClause, R2DBCInsertClause> {

    protected final Configuration configuration;

    protected final R2DBCConnectionProvider connectionProvider;

    public AbstractSQLQueryFactory(Configuration configuration, R2DBCConnectionProvider connectionProvider) {
        this.configuration = configuration;
        this.connectionProvider = connectionProvider;
    }

    @Override
    public final R2DBCDeleteClause delete(RelationalPath<?> path) {
        return new R2DBCDeleteClause(connectionProvider, configuration, path);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final Q from(Expression<?> from) {
        return (Q) query().from(from);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final Q from(Expression<?>... args) {
        return (Q) query().from(args);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final Q from(SubQueryExpression<?> subQuery, Path<?> alias) {
        return (Q) query().from(subQuery, alias);
    }

    @Override
    public final R2DBCInsertClause insert(RelationalPath<?> path) {
        return new R2DBCInsertClause(connectionProvider, configuration, path);
    }


    @Override
    public final R2DBCUpdateClause update(RelationalPath<?> path) {
        return new R2DBCUpdateClause(connectionProvider, configuration, path);
    }

    public final Configuration getConfiguration() {
        return configuration;
    }

    public final Mono<Connection> getConnection() {
        return connectionProvider.getConnection();
    }

    /**
     * Create a new SQL query with the given projection
     *
     * @param expr projection
     * @param <T>  type of the projection
     * @return select(expr)
     */
    public abstract <T> AbstractR2DBCQuery<T, ?> select(Expression<T> expr);

    /**
     * Create a new SQL query with the given projection
     *
     * @param exprs projection
     * @return select(exprs)
     */
    public abstract AbstractR2DBCQuery<Tuple, ?> select(Expression<?>... exprs);

    /**
     * Create a new SQL query with the given projection
     *
     * @param expr distinct projection
     * @param <T>  type of the projection
     * @return select(distinct expr)
     */
    public abstract <T> AbstractR2DBCQuery<T, ?> selectDistinct(Expression<T> expr);

    /**
     * Create a new SQL query with the given projection
     *
     * @param exprs distinct projection
     * @return select(distinct exprs)
     */
    public abstract AbstractR2DBCQuery<Tuple, ?> selectDistinct(Expression<?>... exprs);

    /**
     * Create a new SQL query with zero as the projection
     *
     * @return select(0)
     */
    public abstract AbstractR2DBCQuery<Integer, ?> selectZero();

    /**
     * Create a new SQL query with one as the projection
     *
     * @return select(1)
     */
    public abstract AbstractR2DBCQuery<Integer, ?> selectOne();

    /**
     * Create a new SQL query with the given projection and source
     *
     * @param expr query source and projection
     * @param <T>  type of the projection
     * @return select(expr).from(expr)
     */
    public abstract <T> AbstractR2DBCQuery<T, ?> selectFrom(RelationalPath<T> expr);

}
