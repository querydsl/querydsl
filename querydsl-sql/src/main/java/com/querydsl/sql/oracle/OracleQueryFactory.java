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
import java.util.function.Supplier;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.sql.*;

/**
 * Oracle specific implementation of SQLQueryFactory
 *
 * @author tiwe
 *
 */
public class OracleQueryFactory extends AbstractSQLQueryFactory<OracleQuery<?>> {

    public OracleQueryFactory(Configuration configuration, Supplier<Connection> connection) {
        super(configuration, connection);
    }

    public OracleQueryFactory(Supplier<Connection> connection) {
        this(new Configuration(new OracleTemplates()), connection);
    }

    public OracleQueryFactory(SQLTemplates templates, Supplier<Connection> connection) {
        this(new Configuration(templates), connection);
    }

    @Override
    public OracleQuery<?> query() {
        return new OracleQuery<Void>(connection, configuration);
    }

    @Override
    public <T> OracleQuery<T> select(Expression<T> expr) {
        return query().select(expr);
    }

    @Override
    public OracleQuery<Tuple> select(Expression<?>... exprs) {
        return query().select(exprs);
    }

    @Override
    public <T> OracleQuery<T> selectDistinct(Expression<T> expr) {
        return query().select(expr).distinct();
    }

    @Override
    public OracleQuery<Tuple> selectDistinct(Expression<?>... exprs) {
        return query().select(exprs).distinct();
    }

    @Override
    public OracleQuery<Integer> selectZero() {
        return select(Expressions.ZERO);
    }

    @Override
    public OracleQuery<Integer> selectOne() {
        return select(Expressions.ONE);
    }

    @Override
    public <T> OracleQuery<T> selectFrom(RelationalPath<T> expr) {
        return select(expr).from(expr);
    }

}
