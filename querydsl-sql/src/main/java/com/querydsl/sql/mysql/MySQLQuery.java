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
package com.querydsl.sql.mysql;

import java.sql.Connection;
import java.util.function.Supplier;

import com.querydsl.core.DefaultQueryMetadata;
import com.querydsl.core.QueryMetadata;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.sql.Configuration;
import com.querydsl.sql.MySQLTemplates;
import com.querydsl.sql.SQLTemplates;

/**
 * {@code MySQLQuery} provides MySQL related extensions to SQLQuery.
 *
 * If you need to subtype this, use the base class instead.
 *
 * @param <T> the result type
 */
public class MySQLQuery<T> extends AbstractMySQLQuery<T, MySQLQuery<T>> {
    public MySQLQuery(Connection conn) {
        this(conn, new Configuration(MySQLTemplates.DEFAULT), new DefaultQueryMetadata());
    }

    public MySQLQuery(Connection conn, SQLTemplates templates) {
        this(conn, new Configuration(templates), new DefaultQueryMetadata());
    }

    public MySQLQuery(Connection conn, Configuration configuration) {
        this(conn, configuration, new DefaultQueryMetadata());
    }

    public MySQLQuery(Connection conn, Configuration configuration, QueryMetadata metadata) {
        super(conn, configuration, metadata);
    }

    public MySQLQuery(Supplier<Connection> connProvider, Configuration configuration, QueryMetadata metadata) {
        super(connProvider, configuration, metadata);
    }

    public MySQLQuery(Supplier<Connection> connProvider, Configuration configuration) {
        super(connProvider, configuration, new DefaultQueryMetadata());
    }

    @Override
    public MySQLQuery<T> clone(Connection conn) {
        MySQLQuery<T> q = new MySQLQuery<T>(conn, getConfiguration(), getMetadata().clone());
        q.clone(this);
        return q;
    }

    @Override
    public <U> MySQLQuery<U> select(Expression<U> expr) {
        queryMixin.setProjection(expr);

        @SuppressWarnings("unchecked")
        MySQLQuery<U> res = (MySQLQuery<U>) this;
        return res;
    }

    @Override
    public MySQLQuery<Tuple> select(Expression<?>... exprs) {
        queryMixin.setProjection(exprs);

        @SuppressWarnings("unchecked")
        MySQLQuery<Tuple> res = (MySQLQuery<Tuple>) this;
        return res;
    }

}
