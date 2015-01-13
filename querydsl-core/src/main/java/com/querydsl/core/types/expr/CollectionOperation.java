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

import com.google.common.collect.ImmutableList;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OperationImpl;
import com.querydsl.core.types.Operator;
import com.querydsl.core.types.Visitor;

/**
 * @author tiwe
 *
 */
public class CollectionOperation<E> extends CollectionExpressionBase<Collection<E>, E> {

    private static final long serialVersionUID = 3154315192589335574L;

    private final Class<E> elementType;

    private final OperationImpl<Collection<E>> opMixin;

    public static <E> CollectionOperation<E> create(Operator<?> op, Class<E> type, Expression<?> one) {
        return new CollectionOperation<E>(op, type, ImmutableList.<Expression<?>>of(one));
    }

    public static <E> CollectionOperation<E> create(Operator<?> op, Class<E> type, Expression<?> one, Expression<?> two) {
        return new CollectionOperation<E>(op, type, ImmutableList.of(one, two));
    }

    public static <E> CollectionOperation<E> create(Operator<?> op, Class<E> type, Expression<?>... args) {
        return new CollectionOperation<E>(op, type, args);
    }

    public CollectionOperation(Operator<?> op, Class<? super E> type, Expression<?>... args) {
        this(op, type, ImmutableList.copyOf(args));
    }

    public CollectionOperation(Operator<?> op, Class<? super E> type, ImmutableList<Expression<?>> args) {
        super(new OperationImpl(Collection.class, op, args));
        this.opMixin = (OperationImpl)super.mixin;
        this.elementType = (Class<E>)type;
    }

    @Override
    public Class<?> getParameter(int index) {
        if (index == 0) {
            return elementType;
        } else {
            throw new IndexOutOfBoundsException(String.valueOf(index));
        }
    }

    @Override
    @Nullable
    public <R, C> R accept(Visitor<R, C> v, C context) {
        return opMixin.accept(v, context);
    }

    @Override
    public Class<E> getElementType() {
        return elementType;
    }

}
