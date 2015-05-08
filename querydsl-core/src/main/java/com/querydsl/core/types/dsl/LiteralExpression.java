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

import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Ops;

/**
 * {@code LiteralExpression} represents literal expressions
 *
 * @param <T>
 */
public abstract class LiteralExpression<T extends Comparable> extends ComparableExpression<T> {

    @Nullable
    private transient volatile StringExpression stringCast;

    public LiteralExpression(Expression<T> mixin) {
        super(mixin);
    }

    /**
     * Create a cast expression to the given numeric type
     *
     * @param <A> numeric type
     * @param type numeric type
     * @return cast expression
     */
    public <A extends Number & Comparable<? super A>> NumberExpression<A> castToNum(Class<A> type) {
        return Expressions.numberOperation(type, Ops.NUMCAST, mixin, ConstantImpl.create(type));
    }

    /**
     * Create a cast to String expression
     *
     * @see java.lang.Object#toString()
     * @return cast expression
     */
    public StringExpression stringValue() {
        if (stringCast == null) {
            stringCast = Expressions.stringOperation(Ops.STRING_CAST, mixin);
        }
        return stringCast;
    }

}
