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
package com.querydsl.core.types.dsl;

import java.util.Collection;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;
import com.querydsl.core.types.*;

/**
 * {@code CollectionOperation} is a collection typed operation
 *
 * @author tiwe
 * @param <E> element type
 */
public class CollectionOperation<E> extends CollectionExpressionBase<Collection<E>, E> {

    private static final long serialVersionUID = 3154315192589335574L;

    private final Class<E> elementType;

    private final OperationImpl<Collection<E>> opMixin;

    protected CollectionOperation(Class<? super E> type, Operator op, Expression<?>... args) {
        this(type, op, ImmutableList.copyOf(args));
    }

    protected CollectionOperation(Class<? super E> type, Operator op, ImmutableList<Expression<?>> args) {
        super(ExpressionUtils.operation((Class) Collection.class, op, args));
        this.opMixin = (OperationImpl) super.mixin;
        this.elementType = (Class<E>) type;
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
