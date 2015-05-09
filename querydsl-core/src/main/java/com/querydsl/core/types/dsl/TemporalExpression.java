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

import com.querydsl.core.types.Expression;

/**
 * {@code TemporalExpression} is a supertype for Date/Time related types
 *
 * @author tiwe
 *
 * @param <T> expression type
 */
@SuppressWarnings({"unchecked"})
public abstract class TemporalExpression<T extends Comparable> extends LiteralExpression<T> {

    private static final long serialVersionUID = 1137918766051524298L;

    public TemporalExpression(Expression<T> mixin) {
        super(mixin);
    }

    /**
     * Create a {@code this > right} expression
     *
     * @param right rhs of the comparison
     * @return this &gt; right
     */
    public BooleanExpression after(T right) {
        return gt(right);
    }

    /**
     * Create a {@code this > right} expression
     *
     * @param right rhs of the comparison
     * @return this &gt; right
     */
    public BooleanExpression after(Expression<T> right) {
        return gt(right);
    }

    /**
     * Create a {@code this < right} expression
     *
     * @param right rhs of the comparison
     * @return this &lt; right
     */
    public BooleanExpression before(T right) {
        return lt(right);
    }

    /**
     * Create a {@code this < right} expression
     *
     * @param right rhs of the comparison
     * @return this &lt; right
     */
    public BooleanExpression before(Expression<T> right) {
        return lt(right);
    }

}
