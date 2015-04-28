/*
 * Copyright 2015, Timo Westkämper
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

import com.querydsl.core.types.Expression;

/**
 * {@code FetchableQuery} extends {@link Fetchable} and {@link SimpleQuery} with projection changing
 * methods and result aggregation functionality using {@link ResultTransformer} instances.
 *
 * @param <T> element type
 * @param <Q> concrete subtype
 */
public interface FetchableQuery<T, Q extends FetchableQuery<T, Q>> extends SimpleQuery<Q>, Fetchable<T> {

    /**
     * Change the projection of this query
     *
     * @param <U>
     * @param expr new projection
     *
     * @return the current object
     */
    <U> FetchableQuery<U, ?> select(Expression<U> expr);

    /**
     * Change the projection of this query
     *
     * @param exprs new projection
     * @return the current object
     */
    FetchableQuery<Tuple, ?> select(Expression<?>... exprs);

    /**
     * Apply the given transformer to this {@code FetchableQuery} instance and return the results
     *
     * @param <S>
     * @param transformer result transformer
     * @return transformed result
     */
    <S> S transform(ResultTransformer<S> transformer);

}
