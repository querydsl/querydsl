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
package com.querydsl.sql.postgresql;

import java.sql.Connection;

import com.querydsl.core.DefaultQueryMetadata;
import com.querydsl.core.QueryFlag.Position;
import com.querydsl.core.QueryMetadata;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.sql.AbstractSQLQuery;
import com.querydsl.sql.Configuration;
import com.querydsl.sql.PostgreSQLTemplates;
import com.querydsl.sql.RelationalPath;
import com.querydsl.sql.SQLOps;
import com.querydsl.sql.SQLQuery;
import com.querydsl.sql.SQLTemplates;

/**
 * {@code PostgreSQLQuery} provides PostgreSQL related extensions to SQLQuery
 *
 * @author tiwe
 * @see SQLQuery
 *
 */
public class PostgreSQLQuery<T> extends AbstractSQLQuery<T, PostgreSQLQuery<T>> {

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

    /**
     * FOR SHARE causes the rows retrieved by the SELECT statement to be locked as though for update.
     *
     * @return the current object
     */
    public PostgreSQLQuery<T> forShare() {
        return addFlag(SQLOps.FOR_SHARE_FLAG);
    }

    /**
     * With NOWAIT, the statement reports an error, rather than waiting, if a selected row cannot
     * be locked immediately.
     *
     * @return the current object
     */
    public PostgreSQLQuery<T> noWait() {
        return addFlag(SQLOps.NO_WAIT_FLAG);
    }

    /**
     * FOR UPDATE / FOR SHARE OF tables
     *
     * @param paths tables
     * @return the current object
     */
    public PostgreSQLQuery<T> of(RelationalPath<?>... paths) {
        StringBuilder builder = new StringBuilder(" of ");
        for (RelationalPath<?> path : paths) {
            if (builder.length() > 4) {
                builder.append(", ");
            }
            builder.append(getConfiguration().getTemplates().quoteIdentifier(path.getTableName()));
        }
        return addFlag(Position.END, builder.toString());
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
        return (PostgreSQLQuery<U>) this;
    }

    @Override
    public PostgreSQLQuery<Tuple> select(Expression<?>... exprs) {
        queryMixin.setProjection(exprs);
        return (PostgreSQLQuery<Tuple>) this;
    }
}
