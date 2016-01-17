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
package com.querydsl.sql.mssql;

import java.sql.Connection;

import javax.inject.Provider;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.sql.*;

/**
 * SQL Server specific implementation of SQLQueryFactory
 *
 * @author tiwe
 *
 */
public class SQLServerQueryFactory extends AbstractSQLQueryFactory<SQLServerQuery<?>> {

    public SQLServerQueryFactory(Configuration configuration, Provider<Connection> connection) {
        super(configuration, connection);
    }

    public SQLServerQueryFactory(Provider<Connection> connection) {
        this(new Configuration(new SQLServerTemplates()), connection);
    }

    public SQLServerQueryFactory(SQLTemplates templates, Provider<Connection> connection) {
        this(new Configuration(templates), connection);
    }

    @Override
    public SQLServerQuery<?> query() {
        return new SQLServerQuery<Void>(connection, configuration);
    }

    @Override
    public <T> SQLServerQuery<T> select(Expression<T> expr) {
        return query().select(expr);
    }

    @Override
    public SQLServerQuery<Tuple> select(Expression<?>... exprs) {
        return query().select(exprs);
    }

    @Override
    public <T> SQLServerQuery<T> selectDistinct(Expression<T> expr) {
        return query().select(expr).distinct();
    }

    @Override
    public SQLServerQuery<Tuple> selectDistinct(Expression<?>... exprs) {
        return query().select(exprs).distinct();
    }

    @Override
    public SQLServerQuery<Integer> selectZero() {
        return select(Expressions.ZERO);
    }

    @Override
    public SQLServerQuery<Integer> selectOne() {
        return select(Expressions.ONE);
    }

    @Override
    public <T> SQLServerQuery<T> selectFrom(RelationalPath<T> expr) {
        return select(expr).from(expr);
    }

}
