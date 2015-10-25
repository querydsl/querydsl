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
package com.querydsl.sql;

import java.sql.Connection;

import com.querydsl.core.DefaultQueryMetadata;
import com.querydsl.core.QueryMetadata;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;

/**
 * {@code SQLQuery} is a JDBC based implementation of the {@link SQLCommonQuery}
 * interface
 *
 * @param <T>
 * @author tiwe
 */
public class SQLQuery<T> extends AbstractSQLQuery<T, SQLQuery<T>> {

    /**
     * Create a detached SQLQuery instance The query can be attached via the
     * clone method
     */
    public SQLQuery() {
        super(null, Configuration.DEFAULT, new DefaultQueryMetadata());
    }

    /**
     * Create a detached SQLQuery instance The query can be attached via the
     * clone method
     *
     * @param templates SQLTemplates to use
     */
    public SQLQuery(SQLTemplates templates) {
        super(null, new Configuration(templates), new DefaultQueryMetadata());
    }

    /**
     * Create a new SQLQuery instance
     *
     * @param conn Connection to use
     * @param templates SQLTemplates to use
     */
    public SQLQuery(Connection conn, SQLTemplates templates) {
        super(conn, new Configuration(templates), new DefaultQueryMetadata());
    }

    /**
     * Create a new SQLQuery instance
     *
     * @param conn Connection to use
     * @param templates SQLTemplates to use
     * @param metadata metadata
     */
    public SQLQuery(Connection conn, SQLTemplates templates, QueryMetadata metadata) {
        super(conn, new Configuration(templates), metadata);
    }

    /**
     * Create a new SQLQuery instance
     *
     * @param configuration configuration
     */
    public SQLQuery(Configuration configuration) {
        this(null, configuration);
    }

    /**
     * Create a new SQLQuery instance
     *
     * @param conn Connection to use
     * @param configuration configuration
     */
    public SQLQuery(Connection conn, Configuration configuration) {
        super(conn, configuration, new DefaultQueryMetadata());
    }

    /**
     * Create a new SQLQuery instance
     *
     * @param conn Connection to use
     * @param configuration configuration
     * @param metadata metadata
     */
    public SQLQuery(Connection conn, Configuration configuration, QueryMetadata metadata) {
        super(conn, configuration, metadata);
    }

    @Override
    public SQLQuery<T> clone(Connection conn) {
        SQLQuery<T> q = new SQLQuery<T>(conn, getConfiguration(), getMetadata().clone());
        q.clone(this);
        return q;
    }

    @Override
    public <U> SQLQuery<U> select(Expression<U> expr) {
        queryMixin.setProjection(expr);
        @SuppressWarnings("unchecked") // This is the new type
        SQLQuery<U> newType = (SQLQuery<U>) this;
        return newType;
    }

    @Override
    public SQLQuery<Tuple> select(Expression<?>... exprs) {
        queryMixin.setProjection(exprs);
        @SuppressWarnings("unchecked") // This is the new type
        SQLQuery<Tuple> newType = (SQLQuery<Tuple>) this;
        return newType;
    }
}
