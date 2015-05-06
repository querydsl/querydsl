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

import java.sql.Connection;
import java.sql.SQLException;

import javax.inject.Provider;
import javax.sql.DataSource;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.Expressions;

/**
 * Factory class for query and DML clause creation
 *
 * @author tiwe
 *
 */
public class SQLQueryFactory extends AbstractSQLQueryFactory<SQLQuery<?>> {

    static class DataSourceProvider implements Provider<Connection> {

        private final DataSource ds;

        public DataSourceProvider(DataSource ds) {
            this.ds = ds;
        }

        @Override
        public Connection get() {
            try {
                return ds.getConnection();
            } catch (SQLException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }

    }

    public SQLQueryFactory(SQLTemplates templates, Provider<Connection> connection) {
        this(new Configuration(templates), connection);
    }

    public SQLQueryFactory(Configuration configuration, Provider<Connection> connection) {
        super(configuration, connection);
    }

    public SQLQueryFactory(Configuration configuration, DataSource dataSource) {
        super(configuration, new DataSourceProvider(dataSource));
    }

    @Override
    public SQLQuery<?> query() {
        return new SQLQuery<Void>(connection.get(), configuration);
    }

    /**
     * Create a new SQLQuery instance with the given projection
     *
     * @param expr projetion
     * @param <T>
     * @return select(expr)
     */
    public <T> SQLQuery<T> select(Expression<T> expr) {
        return query().select(expr);
    }

    /**
     * Create a new SQLQuery instance with the given projection
     *
     * @param exprs projection
     * @return select(exprs)
     */
    public SQLQuery<Tuple> select(Expression<?>... exprs) {
        return query().select(exprs);
    }

    /**
     * Create a new SQLQuery instance with the given projection
     *
     * @param expr distinct projection
     * @param <T>
     * @return select(distinct expr)
     */
    public <T> SQLQuery<T> selectDistinct(Expression<T> expr) {
        return query().select(expr).distinct();
    }

    /**
     * Create a new SQLQuery instance with the given projection
     *
     * @param exprs distinct projection
     * @return select(distinct exprs)
     */
    public SQLQuery<Tuple> selectDistinct(Expression<?>... exprs) {
        return query().select(exprs).distinct();
    }

    /**
     * Create a new SQLQuery instance with zero as the projection
     *
     * @return select(0)
     */
    public SQLQuery<Integer> selectZero() {
        return select(Expressions.ZERO);
    }

    /**
     * Create a new SQLQuery instance with one as the projection
     *
     * @return select(1)
     */
    public SQLQuery<Integer> selectOne() {
        return select(Expressions.ONE);
    }

    /**
     * Create a new detached SQLQuery instance with the given projection and source
     *
     * @param expr query source andd projection
     * @param <T>
     * @return select(expr).from(expr)
     */
    public <T> SQLQuery<T> selectFrom(RelationalPath<T> expr) {
        return select(expr).from(expr);
    }

}
