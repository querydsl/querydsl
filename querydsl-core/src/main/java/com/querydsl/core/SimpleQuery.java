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
package com.querydsl.core;

import javax.annotation.Nonnegative;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.ParamExpression;

/**
 * SimpleQuery defines a simple querying interface than {@link Query}
 *
 * @author tiwe
 *
 * @param <Q> concrete subtype
 * @see Query
 */
public interface SimpleQuery<Q extends SimpleQuery<Q>> extends FilteredClause<Q> {

    /**
     * Set the limit / max results for the querydsl results
     *
     * @param limit
     * @return
     */
    Q limit(@Nonnegative long limit);

    /**
     * Set the offset for the querydsl results
     *
     * @param offset
     * @return
     */
    Q offset(@Nonnegative long offset);

    /**
     * Set both limit and offset of the querydsl results
     *
     * @param modifiers
     * @return
     */
    Q restrict(QueryModifiers modifiers);

    /**
     * Add order expressions
     *
     * @param o
     * @return
     */
    Q orderBy(OrderSpecifier<?>... o);

    /**
     * Set the given parameter to the given value
     *
     * @param <T>
     * @param param
     * @param value
     * @return
     */
    <T> Q set(ParamExpression<T> param, T value);
    
    /**
     * Set the Query to return distinct results
     * 
     * @return
     */
    Q distinct();

}
