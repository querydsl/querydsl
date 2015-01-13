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
package com.querydsl.core.types;

import javax.annotation.concurrent.Immutable;
import java.lang.reflect.Array;
import java.util.List;

import com.google.common.collect.ImmutableList;

/**
 * ArrayConstructorExpression extends {@link ExpressionBase} to represent array initializers
 *
 * @author tiwe
 *
 * @param <T> component type
 */
@Immutable
public class ArrayConstructorExpression<T> extends FactoryExpressionBase<T[]> {

    private static final long serialVersionUID = 8667880104290226505L;

    private final Class<T> elementType;

    private final ImmutableList<Expression<?>> args;

    @SuppressWarnings("unchecked")
    public ArrayConstructorExpression(Expression<?>... args) {
        this((Class)Object[].class, (Expression[])args);
    }
    
    @SuppressWarnings("unchecked")
    public ArrayConstructorExpression(Class<T[]> type, Expression<T>... args) {
        super(type);
        this.elementType = (Class<T>)type.getComponentType();
        this.args = ImmutableList.<Expression<?>>copyOf(args);
    }

    public final Class<T> getElementType() {
        return elementType;
    }

    @Override
    public <R,C> R accept(Visitor<R,C> v, C context) {
        return v.visit(this, context);
    }

    @SuppressWarnings("unchecked")
    @Override
    public T[] newInstance(Object... a) {
        if (a.getClass().getComponentType().equals(elementType)) {
            return (T[])a;
        } else {
            T[] rv = (T[]) Array.newInstance(elementType, a.length);
            System.arraycopy(a, 0, rv, 0, a.length);
            return rv;
        }
    }

    @Override
    public List<Expression<?>> getArgs() {
        return args;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj instanceof FactoryExpression<?>) {
            FactoryExpression<?> c = (FactoryExpression<?>)obj;
            return args.equals(c.getArgs()) && getType().equals(c.getType());
        } else {
            return false;
        }
    }

}
