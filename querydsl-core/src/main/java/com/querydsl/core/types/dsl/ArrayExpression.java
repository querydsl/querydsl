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

import javax.annotation.Nonnegative;

import com.querydsl.core.types.Expression;

/**
 * {@code ArrayExpression} defines an interface for array typed expression
 *
 * @author tiwe
 *
 * @param <A> array type
 * @param <T> array element type
 */
public interface ArrayExpression<A, T> extends Expression<A> {

    /**
     * Create a {@code this.size()} expression
     *
     * <p>Returns the size of the array as an expression</p>
     *
     * @return size of array
     */
    NumberExpression<Integer> size();

    /**
     * Create a {@code this[index]} expression
     *
     * <p>Returns the element at the given index</p>
     *
     * @param index zero based index
     * @return element at index
     */
    SimpleExpression<T> get(Expression<Integer> index);

    /**
     * Create a {@code this[index]} expression
     *
     * <p>Returns the element at the given index</p>
     *
     * @param index zero based index
     * @return element at index
     */
    SimpleExpression<T> get(@Nonnegative int index);

}
