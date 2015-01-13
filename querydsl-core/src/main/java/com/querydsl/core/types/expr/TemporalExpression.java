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

import com.querydsl.core.types.Expression;

/**
 * TemporalExpression is a supertype for Date/Time related types
 *
 * @author tiwe
 *
 * @param <T> expression type
 */
@SuppressWarnings({"unchecked"})
public abstract class TemporalExpression<T extends Comparable> extends ComparableExpression<T> {

    private static final long serialVersionUID = 1137918766051524298L;

    public TemporalExpression(Expression<T> mixin) {
        super(mixin);
    }

    /**
     * Get a {@code this > right} expression
     *
     * @param right
     * @return
     */
    public BooleanExpression after(T right) {
        return gt(right);
    }

    /**
     * Get a {@code this > right} expression
     *
     * @param right
     * @return
     */
    public BooleanExpression after(Expression<T> right) {
        return gt(right);
    }

    /**
     * Get a {@code this < right} expression
     *
     * @param right
     * @return
     */
    public BooleanExpression before(T right) {
        return lt(right);
    }

    /**
     * Get a {@code this < right} expression
     *
     * @param right
     * @return
     */
    public BooleanExpression before(Expression<T> right) {
        return lt(right);
    }

}
