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
package com.querydsl.jpa;

import com.querydsl.core.CloseableIterator;
import org.jetbrains.annotations.Nullable;
import javax.persistence.Query;

import com.querydsl.core.types.FactoryExpression;

import java.util.stream.Stream;

/**
 * {@code QueryHandle}r provides injection of provider specific functionality into the query logic
 *
 * @author tiwe
 *
 */
public interface QueryHandler {

    /**
     * Return whether native queries should be created as typed queries
     *
     * @return whether native queries should be created as typed queries
     */
    boolean createNativeQueryTyped();

    /**
     * Iterate the results with the optional projection
     *
     * @param query query
     * @return iterator
     */
    <T> CloseableIterator<T> iterate(Query query, @Nullable FactoryExpression<?> projection);

    /**
     * Stream the results with the optional projection
     *
     * @param query query
     * @return stream
     */
    <T> Stream<T> stream(Query query, @Nullable FactoryExpression<?> projection);

    /**
     * Add the given scalar to the given native query
     *
     * @param query query
     * @param alias alias
     * @param type type
     */
    void addScalar(Query query, String alias, Class<?> type);

    /**
     * Add the given entity to the given native query
     *
     * @param query query
     * @param alias alias
     * @param type type
     */
    void addEntity(Query query, String alias, Class<?> type);

    /**
     * Transform the results of the given query using the given factory expression
     *
     * @param query query
     * @param projection projection
     * @return true, if query as been modified
     */
    boolean transform(Query query, FactoryExpression<?> projection);

    /**
     * Return whether entity projections need to be wrapped
     *
     * @return whether entity projections need to be wrapped
     */
    boolean wrapEntityProjections();

}
