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

import java.util.List;

import com.querydsl.core.types.CollectionExpression;
import com.querydsl.core.types.Expression;
import org.jetbrains.annotations.Range;

/**
 * {@code ListExpression} represents {@code java.util.List} typed expressions
 *
 * @author tiwe
 *
 * @param <E> component type
 * @param <Q> result type for {@code any()} and {@code get(int)} results
 * @see java.util.List
 */
public interface ListExpression<E, Q extends SimpleExpression<? super E>> extends CollectionExpression<List<E>, E> {

    /**
     * Indexed access, gets the element at the given index
     *
     * @param index zero based index
     * @return this.get(index)
     * @see java.util.List#get(int)
     */
    Q get(Expression<Integer> index);

    /**
     * Indexed access, gets the element at the given index
     *
     * @param index zero based index
     * @return this.get(index)
     * @see java.util.List#get(int)
     */
    Q get(@Range(from = 0, to = Integer.MAX_VALUE) int index);
}
