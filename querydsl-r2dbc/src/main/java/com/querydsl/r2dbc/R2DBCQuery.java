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

import com.querydsl.core.DefaultQueryMetadata;
import com.querydsl.core.QueryMetadata;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import io.r2dbc.spi.Connection;

/**
 * {@code SQLQuery} is a JDBC based implementation of the {@link R2DBCCommonQuery}
 * interface
 *
 * @param <T>
 * @author mc_fish
 */
public class R2DBCQuery<T> extends AbstractR2DBCQuery<T, R2DBCQuery<T>> {

    /**
     * Create a detached SQLQuery instance The query can be attached via the
     * clone method
     */
    public R2DBCQuery() {
        super((Connection) null, Configuration.DEFAULT, new DefaultQueryMetadata());
    }

    /**
     * Create a detached SQLQuery instance The query can be attached via the
     * clone method
     *
     * @param templates SQLTemplates to use
     */
    public R2DBCQuery(SQLTemplates templates) {
        super((Connection) null, new Configuration(templates), new DefaultQueryMetadata());
    }

    /**
     * Create a new SQLQuery instance
     *
     * @param conn      Connection to use
     * @param templates SQLTemplates to use
     */
    public R2DBCQuery(Connection conn, SQLTemplates templates) {
        super(conn, new Configuration(templates), new DefaultQueryMetadata());
    }

    /**
     * Create a new SQLQuery instance
     *
     * @param conn      Connection to use
     * @param templates SQLTemplates to use
     * @param metadata  metadata
     */
    public R2DBCQuery(Connection conn, SQLTemplates templates, QueryMetadata metadata) {
        super(conn, new Configuration(templates), metadata);
    }

    /**
     * Create a new SQLQuery instance
     *
     * @param configuration configuration
     */
    public R2DBCQuery(Configuration configuration) {
        this((Connection) null, configuration);
    }

    /**
     * Create a new SQLQuery instance
     *
     * @param conn          Connection to use
     * @param configuration configuration
     */
    public R2DBCQuery(Connection conn, Configuration configuration) {
        super(conn, configuration, new DefaultQueryMetadata());
    }

    /**
     * Create a new SQLQuery instance
     *
     * @param conn          Connection to use
     * @param configuration configuration
     * @param metadata      metadata
     */
    public R2DBCQuery(Connection conn, Configuration configuration, QueryMetadata metadata) {
        super(conn, configuration, metadata);
    }

    /**
     * Create a new SQLQuery instance
     *
     * @param connProvider  Connection to use
     * @param configuration configuration
     */
    public R2DBCQuery(R2DBCConnectionProvider connProvider, Configuration configuration) {
        super(connProvider, configuration, new DefaultQueryMetadata());
    }

    /**
     * Create a new SQLQuery instance
     *
     * @param connProvider  Connection to use
     * @param configuration configuration
     * @param metadata      metadata
     */
    public R2DBCQuery(R2DBCConnectionProvider connProvider, Configuration configuration, QueryMetadata metadata) {
        super(connProvider, configuration, metadata);
    }


    @Override
    public R2DBCQuery<T> clone(Connection conn) {
        R2DBCQuery<T> q = new R2DBCQuery<T>(conn, getConfiguration(), getMetadata().clone());
        q.clone(this);
        return q;
    }

    @Override
    public <U> R2DBCQuery<U> select(Expression<U> expr) {
        queryMixin.setProjection(expr);
        @SuppressWarnings("unchecked") // This is the new type
                R2DBCQuery<U> newType = (R2DBCQuery<U>) this;
        return newType;
    }

    @Override
    public R2DBCQuery<Tuple> select(Expression<?>... exprs) {
        queryMixin.setProjection(exprs);
        @SuppressWarnings("unchecked") // This is the new type
                R2DBCQuery<Tuple> newType = (R2DBCQuery<Tuple>) this;
        return newType;
    }
}
