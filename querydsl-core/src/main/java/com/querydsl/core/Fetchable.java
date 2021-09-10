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
package com.querydsl.core;

import com.mysema.commons.lang.CloseableIterator;

import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * {@code Fetchable} defines default projection methods for {@link Query} implementations.
 * All Querydsl query implementations should implement this interface.
 *
 * @param <T> result type
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
    T fetchOne() throws NonUniqueResultException;

    /**
     * Get the projection as a typed closeable Iterator
     *
     * @return closeable iterator
     */
    CloseableIterator<T> iterate();

    /**
     * Get the projection as a typed closeable Stream.
     *
     * @return closeable stream
     */
    default Stream<T> stream() {
        final CloseableIterator<T> iterator = iterate();
        final Spliterator<T> spliterator = Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED);
        return StreamSupport.stream(spliterator, false)
                .onClose(iterator::close);
    }

    /**
     * Get the projection in {@link QueryResults} form.
     *
     * Make sure to use {@link #fetch()} instead if you do not rely on the {@link QueryResults#getOffset()} or
     * {@link QueryResults#getLimit()}, because it will be more performant. Also, count queries cannot be
     * properly generated for all dialects. For example: in JPA count queries can't be generated for queries
     * that have multiple group by expressions or a having clause.
     * Get the projection in {@link QueryResults} form.
     *
     * Use {@link #fetch()} instead if you do not need the total count of rows in the query result.
     *
     * @return results
     * @see #fetch()
     */
    QueryResults<T> fetchResults();

    /**
     * Get the count of matched elements
     *
     * Note: not all QueryDSL modules might optimize fetchCount using a count query.
     * An implementation is allowed to fall back to {@code fetch().size()}.
     *
     * @return row count
     */
    long fetchCount();

}
