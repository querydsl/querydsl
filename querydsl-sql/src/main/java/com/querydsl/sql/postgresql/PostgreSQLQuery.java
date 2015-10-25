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

import com.querydsl.core.DefaultQueryMetadata;
import com.querydsl.core.QueryFlag;
import com.querydsl.core.QueryFlag.Position;
import com.querydsl.core.QueryMetadata;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.sql.*;

/**
 * {@code PostgreSQLQuery} provides PostgreSQL related extensions to SQLQuery
 *
 * @param <T> result type
 *
 * @see SQLQuery
 * @author tiwe
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
        // global forShare support was added later, delegating to super implementation
        return super.forShare();
    }

    /**
     * With NOWAIT, the statement reports an error, rather than waiting, if a selected row cannot
     * be locked immediately.
     *
     * @return the current object
     */
    public PostgreSQLQuery<T> noWait() {
        QueryFlag noWaitFlag = configuration.getTemplates().getNoWaitFlag();
        return addFlag(noWaitFlag);
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

    /**
     * adds a DISTINCT ON clause
     *
     * @param exprs
     * @return
     */
    public PostgreSQLQuery<T> distinctOn(Expression<?>... exprs) {
        return addFlag(Position.AFTER_SELECT,
            Expressions.template(Object.class, "distinct on({0}) ",
            ExpressionUtils.list(Object.class, exprs)));
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
