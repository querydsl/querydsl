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

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.r2dbc.*;
import com.querydsl.sql.RelationalPath;

/**
 * PostgreSQL specific implementation of SQLQueryFactory
 *
 * @author tiwe
 */
public class R2DBCPostgreQueryFactory extends AbstractR2DBCQueryFactory<R2DBCPostgreQuery<?>> {

    public R2DBCPostgreQueryFactory(Configuration configuration, R2DBCConnectionProvider connection) {
        super(configuration, connection);
    }

    public R2DBCPostgreQueryFactory(R2DBCConnectionProvider connection) {
        this(new Configuration(new PostgreSQLTemplates()), connection);
    }

    public R2DBCPostgreQueryFactory(SQLTemplates templates, R2DBCConnectionProvider connection) {
        this(new Configuration(templates), connection);
    }

    @Override
    public R2DBCPostgreQuery<?> query() {
        return new R2DBCPostgreQuery<Void>(connection, configuration);
    }

    @Override
    public <T> R2DBCPostgreQuery<T> select(Expression<T> expr) {
        return query().select(expr);
    }

    @Override
    public R2DBCPostgreQuery<Tuple> select(Expression<?>... exprs) {
        return query().select(exprs);
    }

    @Override
    public <T> R2DBCPostgreQuery<T> selectDistinct(Expression<T> expr) {
        return query().select(expr).distinct();
    }

    @Override
    public R2DBCPostgreQuery<Tuple> selectDistinct(Expression<?>... exprs) {
        return query().select(exprs).distinct();
    }

    @Override
    public R2DBCPostgreQuery<Integer> selectZero() {
        return select(Expressions.ZERO);
    }

    @Override
    public R2DBCPostgreQuery<Integer> selectOne() {
        return select(Expressions.ONE);
    }

    @Override
    public <T> R2DBCPostgreQuery<T> selectFrom(RelationalPath<T> expr) {
        return select(expr).from(expr);
    }


}
