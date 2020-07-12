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
import com.querydsl.core.types.FactoryExpression;
import com.querydsl.core.types.Projections;
import io.r2dbc.spi.Connection;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author mc_fish
 */
public class ExtendedR2DBCQuery<T> extends AbstractR2DBCQuery<T, ExtendedR2DBCQuery<T>> {

    public ExtendedR2DBCQuery(SQLTemplates templates) {
        super((Connection) null, new Configuration(templates), new DefaultQueryMetadata());
    }

    public ExtendedR2DBCQuery(Connection conn, SQLTemplates templates) {
        super(conn, new Configuration(templates), new DefaultQueryMetadata());
    }

    public ExtendedR2DBCQuery(Connection conn, Configuration configuration) {
        super(conn, configuration, new DefaultQueryMetadata());
    }

    public ExtendedR2DBCQuery(Connection conn, Configuration configuration, QueryMetadata metadata) {
        super(conn, configuration, metadata);
    }

    public <RT> Mono<RT> uniqueResult(Class<RT> type, Expression<?>... exprs) {
        return select(createProjection(type, exprs)).fetchOne();
    }

    public <RT> Flux<RT> list(Class<RT> type, Expression<?>... exprs) {
        return select(createProjection(type, exprs)).fetch();
    }

    private <T> FactoryExpression<T> createProjection(Class<T> type, Expression<?>... exprs) {
        return Projections.bean(type, exprs);
    }

    @Override
    public ExtendedR2DBCQuery<T> clone(Connection connection) {
        ExtendedR2DBCQuery<T> query = new ExtendedR2DBCQuery<T>(connection, getConfiguration(), getMetadata().clone());
        query.clone(this);
        return query;
    }

    @Override
    public <U> ExtendedR2DBCQuery<U> select(Expression<U> expr) {
        queryMixin.setProjection(expr);
        @SuppressWarnings("unchecked") // This is the new type
                ExtendedR2DBCQuery<U> newType = (ExtendedR2DBCQuery<U>) this;
        return newType;
    }

    @Override
    public ExtendedR2DBCQuery<Tuple> select(Expression<?>... exprs) {
        queryMixin.setProjection(exprs);
        @SuppressWarnings("unchecked") // This is the new type
                ExtendedR2DBCQuery<Tuple> newType = (ExtendedR2DBCQuery<Tuple>) this;
        return newType;
    }

}
