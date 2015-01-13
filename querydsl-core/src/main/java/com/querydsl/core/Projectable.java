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
import java.util.Map;

import javax.annotation.Nonnegative;
import javax.annotation.Nullable;

import com.mysema.commons.lang.CloseableIterator;
import com.querydsl.core.types.Expression;

/**
 * Projectable defines default projection methods for {@link Query} implementations.
 * All Querydsl querydsl implementations should implement either this interface or
 * {@link SimpleProjectable}.
 *
 * @author tiwe
 * @see SimpleProjectable
 */
public interface Projectable {
    /**
     * return the amount of matched rows
     */
    @Nonnegative
    long count();

    /**
     * @return true, if rows matching the given criteria exist, otherwise false
     */
    boolean exists();

    /**
     * @return true, if no rows matching the given criteria exist, otherwise false
     */
    boolean notExists();

    /**
     * iterate over the results for the given projection
     *
     * @param args
     * @return
     */
    CloseableIterator<Tuple> iterate(Expression<?>... args);

    /**
     * iterate over the results for the given projection
     *
     * @param <RT>
     *            generic type of the Iterator
     * @param projection
     * @return an Iterator over the projection
     */
    <RT> CloseableIterator<RT> iterate(Expression<RT> projection);

    /**
     * list the results for the given projection
     * 
     * An empty list is returned for no results.
     *
     * @param args
     * @return
     */
    List<Tuple> list(Expression<?>... args);

    /**
     * list the results for the given projection
     * 
     * An empty list is returned for no results.
     *
     * @param <RT>
     *            generic type of the List
     * @param projection
     * @return a List over the projection
     */
    <RT> List<RT> list(Expression<RT> projection);

    /**
     * list the results for the given projection
     *
     * @param args
     * @return
     */
     SearchResults<Tuple> listResults(Expression<?>... args);
    
    /**
     * list the results for the given projection
     *
     * @param <RT>
     * @param projection
     * @return
     */
    <RT> SearchResults<RT> listResults(Expression<RT> projection);

    /**
     * return the given projection as a Map instance using key and value for Map population
     *
     * An empty map is returned for no results.
     *
     * @param <K>
     * @param <V>
     * @param key
     * @param value
     * @return
     */
    <K,V> Map<K,V> map(Expression<K> key, Expression<V> value);

    /**
     * return a single result for the given projection or null if no result is found
     * 
     * <p>There is some ambiguity for missing results and null valued results, for disambiguation 
     * use the list or iterate methods instead.</p> 
     *
     * <p>for multiple results only the first one is returned</p>
     *
     * @param args
     * @return
     */
    @Nullable
    Tuple singleResult(Expression<?>... args);

    /**
     * return a single result for the given projection or null if no result is found
     *
     * <p>There is some ambiguity for missing results and null valued results, for disambiguation 
     * use the list or iterate methods instead.</p>
     *
     * <p>for multiple results only the first one is returned</p>
     *
     * @param <RT>
     *            return type
     * @param projection
     * @return the result or null for an empty result
     */
    @Nullable
    <RT> RT singleResult(Expression<RT> projection);
    
    /**
     * Apply the given transformer to this Projectable instance and return the results
     * 
     * @param <T>
     * @param transformer
     * @return
     */
    <T> T transform(ResultTransformer<T> transformer);

    /**
     * return a unique result for the given projection or null if no result is found
     *
     * <p>There is some ambiguity for missing results and null valued results, for disambiguation 
     * use the list or iterate methods instead.</p>
     *
     * @param args
     * @throws NonUniqueResultException if there is more than one matching result
     * @return
     */
    @Nullable
    Tuple uniqueResult(Expression<?>... args);

    /**
     * return a unique result for the given projection or null if no result is found
     * 
     * <p>There is some ambiguity for missing results and null valued results, for disambiguation 
     * use the list or iterate methods instead.</p>
     *
     * @param <RT>
     *            return type
     * @param projection
     * @throws NonUniqueResultException if there is more than one matching result
     * @return the result or null for an empty result
     */
    @Nullable
    <RT> RT uniqueResult(Expression<RT> projection);

}
