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

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.sql.RelationalPath;

/**
 * Factory class for query and DML clause creation
 *
 * @author mc_fish
 */
public class R2DBCQueryFactory extends AbstractSQLQueryFactory<R2DBCQuery<?>> {

    public R2DBCQueryFactory(SQLTemplates templates, R2DBCConnectionProvider connection) {
        this(new Configuration(templates), connection);
    }

    public R2DBCQueryFactory(Configuration configuration, R2DBCConnectionProvider connProvider) {
        super(configuration, connProvider);
    }

    @Override
    public R2DBCQuery<?> query() {
        return new R2DBCQuery<Void>(connectionProvider, configuration);
    }

    @Override
    public <T> R2DBCQuery<T> select(Expression<T> expr) {
        return query().select(expr);
    }

    @Override
    public R2DBCQuery<Tuple> select(Expression<?>... exprs) {
        return query().select(exprs);
    }

    @Override
    public <T> R2DBCQuery<T> selectDistinct(Expression<T> expr) {
        return query().select(expr).distinct();
    }

    @Override
    public R2DBCQuery<Tuple> selectDistinct(Expression<?>... exprs) {
        return query().select(exprs).distinct();
    }

    @Override
    public R2DBCQuery<Integer> selectZero() {
        return select(Expressions.ZERO);
    }

    @Override
    public R2DBCQuery<Integer> selectOne() {
        return select(Expressions.ONE);
    }

    @Override
    public <T> R2DBCQuery<T> selectFrom(RelationalPath<T> expr) {
        return select(expr).from(expr);
    }

}
