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
package com.querydsl.sql;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.google.common.collect.ImmutableList;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;

/**
 * {@code ForeignKey} defines a foreign key on a table to another table
 *
 * @author tiwe
 *
 * @param <E>
 */
@Immutable
public final class ForeignKey<E> implements Serializable, ProjectionRole<Tuple> {

    private static final long serialVersionUID = 2260578033772289023L;

    private final RelationalPath<?> entity;

    private final ImmutableList<? extends Path<?>> localColumns;

    private final ImmutableList<String> foreignColumns;

    @Nullable
    private transient volatile Expression<Tuple> mixin;

    public ForeignKey(RelationalPath<?> entity, Path<?> localColumn, String foreignColumn) {
        this(entity, ImmutableList.of(localColumn), ImmutableList.of(foreignColumn));
    }

    public ForeignKey(RelationalPath<?> entity, ImmutableList<? extends Path<?>> localColumns,
            ImmutableList<String> foreignColumns) {
        this.entity = entity;
        this.localColumns = localColumns;
        this.foreignColumns = foreignColumns;
    }

    public RelationalPath<?> getEntity() {
        return entity;
    }

    public List<? extends Path<?>> getLocalColumns() {
        return localColumns;
    }

    public List<String> getForeignColumns() {
        return foreignColumns;
    }

    @SuppressWarnings("unchecked")
    public Predicate on(RelationalPath<E> entity) {
        BooleanBuilder builder = new BooleanBuilder();
        for (int i = 0; i < localColumns.size(); i++) {
            Expression<Object> local = (Expression<Object>)localColumns.get(i);
            Expression<?> foreign = ExpressionUtils.path(local.getType(), entity, foreignColumns.get(i));
            builder.and(ExpressionUtils.eq(local,foreign));
        }
        return builder.getValue();
    }

    public BooleanExpression in(SubQueryExpression<Tuple> coll) {
        return Expressions.booleanOperation(Ops.IN, getProjection(), coll);
    }

    @Override
    public Expression<Tuple> getProjection() {
        if (mixin == null) {
            mixin = ExpressionUtils.list(Tuple.class, localColumns);
        }
        return mixin;
    }

}
