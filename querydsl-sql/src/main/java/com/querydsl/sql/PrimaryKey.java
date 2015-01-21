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
import com.querydsl.core.Tuple;
import com.querydsl.core.types.CollectionExpression;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.ProjectionRole;
import com.querydsl.core.types.expr.BooleanExpression;
import com.querydsl.core.types.expr.BooleanOperation;

/**
 * PrimaryKey defines a primary key on table
 *
 * @author tiwe
 */
@Immutable
public final class PrimaryKey<E> implements Serializable, ProjectionRole<Tuple> {

    private static final long serialVersionUID = -6913344535043394649L;

    private final RelationalPath<?> entity;

    private final ImmutableList<? extends Path<?>> localColumns;
    
    @Nullable
    private volatile Expression<Tuple> mixin;

    public PrimaryKey(RelationalPath<?> entity, Path<?>... localColumns) {
        this(entity, ImmutableList.copyOf(localColumns));
    }

    public PrimaryKey(RelationalPath<?> entity, ImmutableList<? extends Path<?>> localColumns) {
        this.entity = entity;
        this.localColumns = localColumns;
        this.mixin = ExpressionUtils.list(Tuple.class, localColumns);
    }

    public RelationalPath<?> getEntity() {
        return entity;
    }

    public List<? extends Path<?>> getLocalColumns() {
        return localColumns;
    }
    
    public BooleanExpression in(CollectionExpression<?,Tuple> coll) {        
        return BooleanOperation.create(Ops.IN, getProjection(), coll);
    }

    @Override
    public Expression<Tuple> getProjection() {
        if (mixin == null) {
            mixin = ExpressionUtils.list(Tuple.class, localColumns);
        }
        return mixin;
    }

}
