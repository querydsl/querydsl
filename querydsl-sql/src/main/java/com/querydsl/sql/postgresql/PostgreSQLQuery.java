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
package com.querydsl.sql.postgresql;

import java.sql.Connection;
import java.util.function.Supplier;

import com.querydsl.core.DefaultQueryMetadata;
import com.querydsl.core.QueryMetadata;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.sql.Configuration;
import com.querydsl.sql.PostgreSQLTemplates;
import com.querydsl.sql.SQLTemplates;

/**
 * {@code PostgreSQLQuery} provides Postgres related extensions to SQLQuery.
 *
 * If you need to subtype this, use the base class instead.
 *
 * @param <T> the result type
 */
public class PostgreSQLQuery<T> extends AbstractPostgreSQLQuery<T, PostgreSQLQuery<T>> {

    public PostgreSQLQuery(Connection conn) {
        this(conn, new Configuration(PostgreSQLTemplates.DEFAULT), new DefaultQueryMetadata());
    }

    public PostgreSQLQuery(Connection conn, SQLTemplates templates) {
        this(conn, new Configuration(templates), new DefaultQueryMetadata());
    }

    public PostgreSQLQuery(Connection conn, Configuration configuration) {
        this(conn, configuration, new DefaultQueryMetadata());
    }

    public PostgreSQLQuery(Connection conn, Configuration configuration, QueryMetadata metadata) {
        super(conn, configuration, metadata);
    }

    public PostgreSQLQuery(Supplier<Connection> connProvider, Configuration configuration, QueryMetadata metadata) {
        super(connProvider, configuration, metadata);
    }

    public PostgreSQLQuery(Supplier<Connection> connProvider, Configuration configuration) {
        super(connProvider, configuration, new DefaultQueryMetadata());
    }


    @Override
    public PostgreSQLQuery<T> clone(Connection conn) {
        PostgreSQLQuery<T> q = new PostgreSQLQuery<T>(conn, getConfiguration(), getMetadata().clone());
        q.clone(this);
        return q;
    }

    @Override
    public <U> PostgreSQLQuery<U> select(Expression<U> expr) {
        queryMixin.setProjection(expr);
        @SuppressWarnings("unchecked") // This is the new type
                PostgreSQLQuery<U> newType = (PostgreSQLQuery<U>) this;
        return newType;
    }

    @Override
    public PostgreSQLQuery<Tuple> select(Expression<?>... exprs) {
        queryMixin.setProjection(exprs);
        @SuppressWarnings("unchecked") // This is the new type
                PostgreSQLQuery<Tuple> newType = (PostgreSQLQuery<Tuple>) this;
        return newType;
    }
}
