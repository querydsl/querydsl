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
package com.querydsl.core.types.expr;

import java.util.Collection;

import javax.annotation.Nullable;

import com.querydsl.core.types.CollectionExpression;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Ops;

/**
 * CollectionExpressionBase is an abstract base class for {@link CollectionExpression} implementations
 *
 * @author tiwe
 *
 * @param <T> expression type
 * @param <E> collection element type
 */
public abstract class CollectionExpressionBase<T extends Collection<E>, E> extends DslExpression<T> implements CollectionExpression<T, E> {

    private static final long serialVersionUID = 691230660037162054L;

    @Nullable
    private volatile BooleanExpression empty;

    @Nullable
    private volatile NumberExpression<Integer> size;

    public CollectionExpressionBase(Expression<T> mixin) {
        super(mixin);
    }

    public DslExpression<E> as(EntityPath<E> alias) {
        return DslOperation.create(getElementType(), Ops.ALIAS, mixin, alias);
    }

    public final BooleanExpression contains(E child) {
        return contains(ConstantImpl.create(child));
    }

    public final BooleanExpression contains(Expression<E> child) {
        return BooleanOperation.create(Ops.IN, child, mixin);
    }

    public abstract Class<E> getElementType();

    public final BooleanExpression isEmpty() {
        if (empty == null) {
            empty = BooleanOperation.create(Ops.COL_IS_EMPTY, mixin);
        }
        return empty;
    }

    public final BooleanExpression isNotEmpty() {
        return isEmpty().not();
    }

    public final NumberExpression<Integer> size() {
        if (size == null) {
            size = NumberOperation.create(Integer.class, Ops.COL_SIZE, mixin);
        }
        return size;
    }

}
