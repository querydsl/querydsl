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

import javax.annotation.Nullable;

import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;

/**
 * ComparableExpressionBase represents comparable expressions
 *
 * @author tiwe
 *
 * @param <T> Java type
 * @see java.lang.Comparable
 */
@SuppressWarnings({"unchecked"})
public abstract class ComparableExpressionBase<T extends Comparable> extends SimpleExpression<T> {

    private static final long serialVersionUID = 1460921109546656911L;

    @Nullable
    private volatile OrderSpecifier<T> asc, desc;

    @Nullable
    private volatile StringExpression stringCast;

    public ComparableExpressionBase(Expression<T> mixin) {
        super(mixin);
    }

    /**
     * Get an OrderSpecifier for ascending order of this expression
     *
     * @return
     */
    public final OrderSpecifier<T> asc() {
        if (asc == null) {
            asc = new OrderSpecifier<T>(Order.ASC, mixin);
        }
        return asc;
    }

    public final Coalesce<T> coalesce(Expression<?>...exprs) {
        Coalesce<T> coalesce = new Coalesce<T>(getType(), mixin);
        for (Expression expr : exprs) {
            coalesce.add(expr);
        }
        return coalesce;
    }

    public final Coalesce<T> coalesce(T... args) {
        Coalesce<T> coalesce = new Coalesce<T>(getType(), mixin);
        for (T arg : args) {
            coalesce.add(arg);
        }
        return coalesce;
    }

    /**
     * Create a cast expression to the given numeric type
     *
     * @param <A>
     * @param type
     * @return
     */
    public <A extends Number & Comparable<? super A>> NumberExpression<A> castToNum(Class<A> type) {
        return NumberOperation.create(type, Ops.NUMCAST, mixin, ConstantImpl.create(type));
    }

    /**
     * Get an OrderSpecifier for descending order of this expression
     *
     * @return
     */
    public final OrderSpecifier<T> desc() {
        if (desc == null) {
            desc = new OrderSpecifier<T>(Order.DESC, mixin);
        }
        return desc;
    }

    /**
     * Get a cast to String expression
     *
     * @see     java.lang.Object#toString()
     * @return
     */
    public StringExpression stringValue() {
        if (stringCast == null) {
            stringCast = StringOperation.create(Ops.STRING_CAST, mixin);
        }
        return stringCast;
    }

}
