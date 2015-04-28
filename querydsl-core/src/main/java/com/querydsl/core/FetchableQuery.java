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
 *
 * @param <T>
 * @param <Q>
 */
public interface FetchableQuery<T, Q extends FetchableQuery<T, Q>> extends SimpleQuery<Q>, Fetchable<T> {

    /**
     *
     * @param expr
     * @param <U>
     * @return
     */
    <U> FetchableQuery<U, ?> select(Expression<U> expr);

    /**
     *
     * @param exprs
     * @return
     */
    FetchableQuery<Tuple, ?> select(Expression<?>... exprs);

    /**
     * Apply the given transformer to this Projectable instance and return the results
     *
     * @param <T>
     * @param transformer
     * @return
     */
    <T> T transform(ResultTransformer<T> transformer);

}
