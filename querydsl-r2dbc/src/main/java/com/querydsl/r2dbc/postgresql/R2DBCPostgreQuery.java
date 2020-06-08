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
package com.querydsl.r2dbc.postgresql;

import com.querydsl.core.DefaultQueryMetadata;
import com.querydsl.core.QueryMetadata;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.r2dbc.Configuration;
import com.querydsl.r2dbc.PostgreSQLTemplates;
import com.querydsl.r2dbc.R2DBCConnectionProvider;
import com.querydsl.r2dbc.SQLTemplates;
import io.r2dbc.spi.Connection;

/**
 * {@code PostgreSQLQuery} provides Postgres related extensions to SQLQuery.
 * <p>
 * If you need to subtype this, use the base class instead.
 *
 * @param <T> the result type
 */
public class R2DBCPostgreQuery<T> extends AbstractR2DBCPostgreQuery<T, R2DBCPostgreQuery<T>> {

    public R2DBCPostgreQuery(Connection conn) {
        this(conn, new Configuration(PostgreSQLTemplates.DEFAULT), new DefaultQueryMetadata());
    }

    public R2DBCPostgreQuery(Connection conn, SQLTemplates templates) {
        this(conn, new Configuration(templates), new DefaultQueryMetadata());
    }

    public R2DBCPostgreQuery(Connection conn, Configuration configuration) {
        this(conn, configuration, new DefaultQueryMetadata());
    }

    public R2DBCPostgreQuery(Connection conn, Configuration configuration, QueryMetadata metadata) {
        super(conn, configuration, metadata);
    }

    public R2DBCPostgreQuery(R2DBCConnectionProvider connProvider, Configuration configuration, QueryMetadata metadata) {
        super(connProvider, configuration, metadata);
    }

    public R2DBCPostgreQuery(R2DBCConnectionProvider connProvider, Configuration configuration) {
        super(connProvider, configuration, new DefaultQueryMetadata());
    }


    @Override
    public R2DBCPostgreQuery<T> clone(Connection conn) {
        R2DBCPostgreQuery<T> q = new R2DBCPostgreQuery<T>(conn, getConfiguration(), getMetadata().clone());
        q.clone(this);
        return q;
    }

    @Override
    public <U> R2DBCPostgreQuery<U> select(Expression<U> expr) {
        queryMixin.setProjection(expr);
        @SuppressWarnings("unchecked") // This is the new type
                R2DBCPostgreQuery<U> newType = (R2DBCPostgreQuery<U>) this;
        return newType;
    }

    @Override
    public R2DBCPostgreQuery<Tuple> select(Expression<?>... exprs) {
        queryMixin.setProjection(exprs);
        @SuppressWarnings("unchecked") // This is the new type
                R2DBCPostgreQuery<Tuple> newType = (R2DBCPostgreQuery<Tuple>) this;
        return newType;
    }
}
