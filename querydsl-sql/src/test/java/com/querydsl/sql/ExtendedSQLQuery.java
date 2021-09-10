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
import java.util.List;

import com.mysema.commons.lang.CloseableIterator;
import com.querydsl.core.*;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.FactoryExpression;
import com.querydsl.core.types.Projections;

/**
 * @author tiwe
 *
 */
public class ExtendedSQLQuery<T> extends AbstractSQLQuery<T, ExtendedSQLQuery<T>> {

    public ExtendedSQLQuery(SQLTemplates templates) {
        super((Connection) null, new Configuration(templates), new DefaultQueryMetadata());
    }

    public ExtendedSQLQuery(Connection conn, SQLTemplates templates) {
        super(conn, new Configuration(templates), new DefaultQueryMetadata());
    }

    public ExtendedSQLQuery(Connection conn, Configuration configuration) {
        super(conn, configuration, new DefaultQueryMetadata());
    }

    public ExtendedSQLQuery(Connection conn, Configuration configuration, QueryMetadata metadata) {
        super(conn, configuration, metadata);
    }

    public <RT> CloseableIterator<RT> iterate(Class<RT> type, Expression<?>... exprs) {
        return select(createProjection(type, exprs)).iterate();
    }

    public <RT> RT uniqueResult(Class<RT> type, Expression<?>... exprs) {
        return select(createProjection(type, exprs)).fetchOne();
    }

    public <RT> List<RT> list(Class<RT> type, Expression<?>... exprs) {
        return select(createProjection(type, exprs)).fetch();
    }

    public <RT> QueryResults<RT> listResults(Class<RT> type, Expression<?>... exprs) {
        return select(createProjection(type, exprs)).fetchResults();
    }

    private <T> FactoryExpression<T> createProjection(Class<T> type, Expression<?>... exprs) {
        return Projections.bean(type, exprs);
    }

    @Override
    public ExtendedSQLQuery<T> clone(Connection connection) {
        ExtendedSQLQuery<T> query = new ExtendedSQLQuery<T>(connection, getConfiguration(), getMetadata().clone());
        query.clone(this);
        return query;
    }

    @Override
    public <U> ExtendedSQLQuery<U> select(Expression<U> expr) {
        queryMixin.setProjection(expr);
        @SuppressWarnings("unchecked") // This is the new type
        ExtendedSQLQuery<U> newType = (ExtendedSQLQuery<U>) this;
        return newType;
    }

    @Override
    public ExtendedSQLQuery<Tuple> select(Expression<?>... exprs) {
        queryMixin.setProjection(exprs);
        @SuppressWarnings("unchecked") // This is the new type
        ExtendedSQLQuery<Tuple> newType = (ExtendedSQLQuery<Tuple>) this;
        return newType;
    }

}
