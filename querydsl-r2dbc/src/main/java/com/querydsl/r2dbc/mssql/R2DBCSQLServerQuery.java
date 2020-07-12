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
package com.querydsl.r2dbc.mssql;

import com.querydsl.core.DefaultQueryMetadata;
import com.querydsl.core.QueryMetadata;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.r2dbc.Configuration;
import com.querydsl.r2dbc.R2DBCConnectionProvider;
import com.querydsl.r2dbc.SQLServerTemplates;
import com.querydsl.r2dbc.SQLTemplates;
import io.r2dbc.spi.Connection;

/**
 * {@code SQLServerQuery} provides SQL Server related extensions to SQLQuery
 * <p>
 * If you need to subtype this, use the base class instead.
 *
 * @param <T> result type
 * @author mc_fish
 */
public class R2DBCSQLServerQuery<T> extends AbstractR2DBCSQLServerQuery<T, R2DBCSQLServerQuery<T>> {

    public R2DBCSQLServerQuery(Connection conn) {
        this(conn, SQLServerTemplates.DEFAULT, new DefaultQueryMetadata());
    }

    public R2DBCSQLServerQuery(Connection conn, SQLTemplates templates) {
        this(conn, templates, new DefaultQueryMetadata());
    }

    protected R2DBCSQLServerQuery(Connection conn, SQLTemplates templates, QueryMetadata metadata) {
        super(conn, new Configuration(templates), metadata);
    }

    public R2DBCSQLServerQuery(Connection conn, Configuration configuration, QueryMetadata metadata) {
        super(conn, configuration, metadata);
    }

    public R2DBCSQLServerQuery(Connection conn, Configuration configuration) {
        super(conn, configuration, new DefaultQueryMetadata());
    }

    public R2DBCSQLServerQuery(R2DBCConnectionProvider connProvider, Configuration configuration, QueryMetadata metadata) {
        super(connProvider, configuration, metadata);
    }

    public R2DBCSQLServerQuery(R2DBCConnectionProvider connProvider, Configuration configuration) {
        super(connProvider, configuration, new DefaultQueryMetadata());
    }

    @Override
    public R2DBCSQLServerQuery<T> clone(Connection conn) {
        R2DBCSQLServerQuery<T> q = new R2DBCSQLServerQuery<T>(conn, getConfiguration(), getMetadata().clone());
        q.clone(this);
        return q;
    }

    @Override
    public <U> R2DBCSQLServerQuery<U> select(Expression<U> expr) {
        queryMixin.setProjection(expr);
        @SuppressWarnings("unchecked") // This is the new type
                R2DBCSQLServerQuery<U> newType = (R2DBCSQLServerQuery<U>) this;
        return newType;
    }

    @Override
    public R2DBCSQLServerQuery<Tuple> select(Expression<?>... exprs) {
        queryMixin.setProjection(exprs);
        @SuppressWarnings("unchecked") // This is the new type
                R2DBCSQLServerQuery<Tuple> newType = (R2DBCSQLServerQuery<Tuple>) this;
        return newType;
    }

}