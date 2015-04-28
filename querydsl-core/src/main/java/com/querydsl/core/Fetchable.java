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

import java.util.List;

import com.mysema.commons.lang.CloseableIterator;

/**
 * {@code Fetchable} defines default projection methods for {@link Query} implementations.
 * All Querydsl query implementations should implement this interface.
 *
 * @author tiwe
 */
public interface Fetchable<T> {
    /**
     * Get the projection as a typed List
     *
     * @return results in list form
     */
    List<T> fetch();

    /**
     * Get the first result of Get the projection or null if no result is found
     *
     * @return first result or null
     */
    T fetchFirst();

    /**
     * Get the projection as a unique result or null if no result is found
     *
     * @throws NonUniqueResultException if there is more than one matching result
     * @return first result or null
     */
    T fetchOne();

    /**
     * Get the projection as a typed closeable Iterator
     *
     * @return closeable iterator
     */
    CloseableIterator<T> iterate();

    /**
     * Get the projection in {@link QueryResults} form
     *
     * @return results
     */
    QueryResults<T> fetchResults();

    /**
     * Get the count of matched elements
     *
     * @return row count
     */
    long fetchCount();

}
