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

import com.querydsl.core.DefaultQueryMetadata;
import com.querydsl.core.QueryFlag;
import com.querydsl.core.QueryMetadata;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.sql.Configuration;
import com.querydsl.sql.SQLOps;
import com.querydsl.sql.SQLTemplates;
import com.querydsl.sql.TeradataTemplates;

import javax.inject.Provider;
import java.sql.Connection;

/**
 * {@code TeradataQuery} provides Teradata related extensions to SQLQuery
 *
 * If you need to subtype this, use the base class instead.
 *
 * @param <T> result type
 *
 * @author tiwe
 */
public class TeradataQuery<T> extends AbstractTeradataQuery<T, TeradataQuery<T>> {

    public TeradataQuery(Connection conn) {
        this(conn, new Configuration(TeradataTemplates.DEFAULT), new DefaultQueryMetadata());
    }

    public TeradataQuery(Connection conn, SQLTemplates templates) {
        this(conn, new Configuration(templates), new DefaultQueryMetadata());
    }

    public TeradataQuery(Connection conn, Configuration configuration) {
        this(conn, configuration, new DefaultQueryMetadata());
    }

    public TeradataQuery(Connection conn, Configuration configuration, QueryMetadata metadata) {
        super(conn, configuration, metadata);
    }

    public TeradataQuery(Provider<Connection> connProvider, Configuration configuration, QueryMetadata metadata) {
        super(connProvider, configuration, metadata);
    }

    public TeradataQuery(Provider<Connection> connProvider, Configuration configuration) {
        super(connProvider, configuration);
    }

    /**
     * Adds a qualify expression
     *
     * @param predicate qualify expression
     * @return the current object
     */
    public TeradataQuery<T> qualify(Predicate predicate) {
        predicate = ExpressionUtils.predicate(SQLOps.QUALIFY, predicate);
        return queryMixin.addFlag(new QueryFlag(QueryFlag.Position.BEFORE_ORDER, predicate));
    }

    @Override
    public TeradataQuery<T> clone(Connection conn) {
        TeradataQuery<T> q = new TeradataQuery<T>(conn, getConfiguration(), getMetadata().clone());
        q.clone(this);
        return q;
    }

    @Override
    public <U> TeradataQuery<U> select(Expression<U> expr) {
        queryMixin.setProjection(expr);
        @SuppressWarnings("unchecked") // This is the new type
        TeradataQuery<U> newType = (TeradataQuery<U>) this;
        return newType;
    }

    @Override
    public TeradataQuery<Tuple> select(Expression<?>... exprs) {
        queryMixin.setProjection(exprs);
        @SuppressWarnings("unchecked") // This is the new type
        TeradataQuery<Tuple> newType = (TeradataQuery<Tuple>) this;
        return newType;
    }

}
