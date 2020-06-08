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

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.r2dbc.*;
import com.querydsl.sql.RelationalPath;

/**
 * SQL Server specific implementation of SQLQueryFactory
 *
 * @author tiwe
 */
public class R2DBCServerQueryFactory extends AbstractR2DBCQueryFactory<R2DBCSQLServerQuery<?>> {

    public R2DBCServerQueryFactory(Configuration configuration, R2DBCConnectionProvider connection) {
        super(configuration, connection);
    }

    public R2DBCServerQueryFactory(R2DBCConnectionProvider connection) {
        this(new Configuration(new SQLServerTemplates()), connection);
    }

    public R2DBCServerQueryFactory(SQLTemplates templates, R2DBCConnectionProvider connection) {
        this(new Configuration(templates), connection);
    }

    @Override
    public R2DBCSQLServerQuery<?> query() {
        return new R2DBCSQLServerQuery<Void>(connection, configuration);
    }

    @Override
    public <T> R2DBCSQLServerQuery<T> select(Expression<T> expr) {
        return query().select(expr);
    }

    @Override
    public R2DBCSQLServerQuery<Tuple> select(Expression<?>... exprs) {
        return query().select(exprs);
    }

    @Override
    public <T> R2DBCSQLServerQuery<T> selectDistinct(Expression<T> expr) {
        return query().select(expr).distinct();
    }

    @Override
    public R2DBCSQLServerQuery<Tuple> selectDistinct(Expression<?>... exprs) {
        return query().select(exprs).distinct();
    }

    @Override
    public R2DBCSQLServerQuery<Integer> selectZero() {
        return select(Expressions.ZERO);
    }

    @Override
    public R2DBCSQLServerQuery<Integer> selectOne() {
        return select(Expressions.ONE);
    }

    @Override
    public <T> R2DBCSQLServerQuery<T> selectFrom(RelationalPath<T> expr) {
        return select(expr).from(expr);
    }

}
