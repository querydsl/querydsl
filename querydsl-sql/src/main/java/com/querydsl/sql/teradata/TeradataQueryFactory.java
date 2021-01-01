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
package com.querydsl.sql.teradata;

import java.sql.Connection;
import java.util.function.Supplier;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.sql.*;

/**
 * Teradata specific implementation of SQLQueryFactory
 *
 * @author tiwe
 *
 */
public class TeradataQueryFactory extends AbstractSQLQueryFactory<TeradataQuery<?>> {

    public TeradataQueryFactory(Configuration configuration, Supplier<Connection> connection) {
        super(configuration, connection);
    }

    public TeradataQueryFactory(Supplier<Connection> connection) {
        this(new Configuration(new TeradataTemplates()), connection);
    }

    public TeradataQueryFactory(SQLTemplates templates, Supplier<Connection> connection) {
        this(new Configuration(templates), connection);
    }

    @Override
    public TeradataQuery<?> query() {
        return new TeradataQuery<Void>(connection, configuration);
    }

    @Override
    public <T> TeradataQuery<T> select(Expression<T> expr) {
        return query().select(expr);
    }

    @Override
    public TeradataQuery<Tuple> select(Expression<?>... exprs) {
        return query().select(exprs);
    }

    @Override
    public <T> TeradataQuery<T> selectDistinct(Expression<T> expr) {
        return query().select(expr).distinct();
    }

    @Override
    public TeradataQuery<Tuple> selectDistinct(Expression<?>... exprs) {
        return query().select(exprs).distinct();
    }

    @Override
    public TeradataQuery<Integer> selectZero() {
        return select(Expressions.ZERO);
    }

    @Override
    public TeradataQuery<Integer> selectOne() {
        return select(Expressions.ONE);
    }

    @Override
    public <T> TeradataQuery<T> selectFrom(RelationalPath<T> expr) {
        return select(expr).from(expr);
    }


}
