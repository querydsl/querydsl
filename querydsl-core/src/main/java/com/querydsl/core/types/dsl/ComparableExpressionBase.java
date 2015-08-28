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

import javax.annotation.Nullable;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;

/**
 * {@code ComparableExpressionBase} represents comparable expressions
 *
 * @author tiwe
 *
 * @param <T> Java type
 * @see java.lang.Comparable
 */
public abstract class ComparableExpressionBase<T extends Comparable> extends SimpleExpression<T> {

    private static final long serialVersionUID = 1460921109546656911L;

    @Nullable
    private transient volatile OrderSpecifier<T> asc, desc;

    public ComparableExpressionBase(Expression<T> mixin) {
        super(mixin);
    }

    /**
     * Create an OrderSpecifier for ascending order of this expression
     *
     * @return ascending order by this
     */
    public final OrderSpecifier<T> asc() {
        if (asc == null) {
            asc = new OrderSpecifier<T>(Order.ASC, mixin);
        }
        return asc;
    }

    /**
     * Create a {@code coalesce(this, exprs...)} expression
     *
     * @param exprs additional arguments
     * @return coalesce
     */
    @SuppressWarnings("unchecked")
    public final Coalesce<T> coalesce(Expression<?>...exprs) {
        Coalesce<T> coalesce = new Coalesce<T>(getType(), mixin);
        for (Expression expr : exprs) {
            coalesce.add(expr);
        }
        return coalesce;
    }

    /**
     * Create a {@code coalesce(this, args...)} expression
     *
     * @param args additional arguments
     * @return coalesce
     */
    public final Coalesce<T> coalesce(T... args) {
        Coalesce<T> coalesce = new Coalesce<T>(getType(), mixin);
        for (T arg : args) {
            coalesce.add(arg);
        }
        return coalesce;
    }

    /**
     * Create an OrderSpecifier for descending order of this expression
     *
     * @return descending order by this
     */
    public final OrderSpecifier<T> desc() {
        if (desc == null) {
            desc = new OrderSpecifier<T>(Order.DESC, mixin);
        }
        return desc;
    }

}
