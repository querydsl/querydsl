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
package com.querydsl.core.group;

import java.util.*;

import com.mysema.commons.lang.Pair;
import com.querydsl.core.ResultTransformer;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;

/**
 * {@code GroupBy} provides factory methods for {@link ResultTransformer} and {@link GroupExpression} creation
 *
 * <p>Example: Group employees by department</p>
 *
 * <pre>{@code
 * query.transform(
 *     GroupBy.groupBy(employee.department)
 *            .as(GroupBy.list(Projections.tuple(employee.id, employee.firstName, employee.lastName))))
 * }</pre>
 *
 *
 * @author sasa
 * @author tiwe
 *
 */
public final class GroupBy {

    /**
     * Create a new GroupByBuilder for the given key expression
     *
     * @param key key for aggregation
     * @return builder for further specification
     */
    public static <K> GroupByBuilder<K> groupBy(Expression<K> key) {
        return new GroupByBuilder<K>(key);
    }

    /**
     * Create a new GroupByBuilder for the given key expressions
     *
     * @param keys keys for aggregation
     * @return builder for further specification
     */
    public static GroupByBuilder<List<?>> groupBy(Expression<?>... keys) {
        return new GroupByBuilder<List<?>>(Projections.list(keys));
    }

    /**
     * Create a new aggregating min expression
     *
     * @param expression expression for which the minimum value will be used in the group by projection
     * @return wrapper expression
     */
    public static <E extends Comparable<? super E>> AbstractGroupExpression<E, E> min(Expression<E> expression) {
        return new GMin<E>(expression);
    }

    /**
     * Create a new aggregating sum expression
     *
     * @param expression expression a for which the accumulated sum will be used in the group by projection
     * @return wrapper expression
     */
    public static <E extends Number> AbstractGroupExpression<E, E> sum(Expression<E> expression) {
        return new GSum<E>(expression);
    }

    /**
     * Create a new aggregating avg expression
     *
     * @param expression expression for which the accumulated average value will be used in the group by projection
     * @return wrapper expression
     */
    public static <E extends Number> AbstractGroupExpression<E, E> avg(Expression<E> expression) {
        return new GAvg<E>(expression);
    }

    /**
     * Create a new aggregating max expression
     *
     * @param expression expression for which the maximum value will be used in the group by projection
     * @return wrapper expression
     */
    public static <E extends Comparable<? super E>> AbstractGroupExpression<E, E> max(Expression<E> expression) {
        return new GMax<E>(expression);
    }

    /**
     * Create a new aggregating list expression
     *
     * @param expression values for this expression will be accumulated into a list
     * @return wrapper expression
     */
    public static <E> AbstractGroupExpression<E, List<E>> list(Expression<E> expression) {
        return new GList<E>(expression);
    }

    /**
     * Create a new aggregating list expression
     *
     * @param groupExpression values for this expression will be accumulated into a list
     * @param <E>
     * @param <F>
     * @return wrapper expression
     */
    public static <E, F> AbstractGroupExpression<E, List<F>> list(GroupExpression<E, F> groupExpression) {
        return new MixinGroupExpression<E, F, List<F>>(groupExpression, new GList<F>(groupExpression));
    }

    /**
     * Create a new aggregating set expression using a backing LinkedHashSet
     *
     * @param expression values for this expression will be accumulated into a set
     * @return wrapper expression
     */
    public static <E> AbstractGroupExpression<E, Set<E>> set(Expression<E> expression) {
        return GSet.createLinked(expression);
    }

    /**
     * Create a new aggregating set expression using a backing LinkedHashSet
     *
     * @param groupExpression values for this expression will be accumulated into a set
     * @param <E>
     * @param <F>
     * @return wrapper expression
     */
    public static <E, F> GroupExpression<E, Set<F>> set(GroupExpression<E, F> groupExpression) {
        return new MixinGroupExpression<E, F, Set<F>>(groupExpression, GSet.createLinked(groupExpression));
    }

    /**
     * Create a new aggregating set expression using a backing TreeSet
     *
     * @param expression values for this expression will be accumulated into a set
     * @return wrapper expression
     */
    public static <E extends Comparable<? super E>> AbstractGroupExpression<E, SortedSet<E>> sortedSet(Expression<E> expression) {
        return GSet.createSorted(expression);
    }

    /**
     * Create a new aggregating set expression using a backing TreeSet
     *
     * @param groupExpression values for this expression will be accumulated into a set
     * @return wrapper expression
     */
    public static <E, F extends Comparable<? super F>> GroupExpression<E, SortedSet<F>> sortedSet(GroupExpression<E, F> groupExpression) {
        return new MixinGroupExpression<E, F, SortedSet<F>>(groupExpression, GSet.createSorted(groupExpression));
    }


    /**
     * Create a new aggregating set expression using a backing TreeSet using the given comparator
     *
     * @param expression values for this expression will be accumulated into a set
     * @param comparator comparator of the created TreeSet instance
     * @return wrapper expression
     */
    public static <E> AbstractGroupExpression<E, SortedSet<E>> sortedSet(
            Expression<E> expression, Comparator<? super E> comparator) {

        return GSet.createSorted(expression, comparator);
    }

    /**
     * Create a new aggregating set expression using a backing TreeSet using the given comparator
     *
     * @param groupExpression values for this expression will be accumulated into a set
     * @param comparator comparator of the created TreeSet instance
     * @return wrapper expression
     */
    public static <E, F> GroupExpression<E, SortedSet<F>> sortedSet(
            GroupExpression<E, F> groupExpression, Comparator<? super F> comparator) {

        return new MixinGroupExpression<E, F, SortedSet<F>>(groupExpression, GSet.createSorted(groupExpression, comparator));
    }


    /**
     * Create a new aggregating map expression using a backing LinkedHashMap
     *
     * @param key key for the map entries
     * @param value value for the map entries
     * @return wrapper expression
     */
    public static <K, V> AbstractGroupExpression<Pair<K, V>,Map<K, V>> map(
            Expression<K> key, Expression<V> value) {

        return GMap.createLinked(QPair.create(key, value));
    }

    /**
     * Create a new aggregating map expression using a backing LinkedHashMap
     *
     * @param key key for the map entries
     * @param value value for the map entries
     * @return wrapper expression
     */
    public static <K, V, T> AbstractGroupExpression<Pair<K, V>, Map<T, V>> map(
            GroupExpression<K, T> key, Expression<V> value) {

        return map(key, new GOne<V>(value));
    }

    /**
     * Create a new aggregating map expression using a backing LinkedHashMap
     *
     * @param key key for the map entries
     * @param value value for the map entries
     * @return wrapper expression
     */
    public static <K, V, U> AbstractGroupExpression<Pair<K, V>, Map<K, U>> map(
            Expression<K> key, GroupExpression<V, U> value) {

        return map(new GOne<K>(key), value);
    }

    /**
     * Create a new aggregating map expression using a backing LinkedHashMap
     *
     * @param key key for the map entries
     * @param value value for the map entries
     * @return wrapper expression
     */
    public static <K, V, T, U> AbstractGroupExpression<Pair<K, V>, Map<T, U>> map(
            GroupExpression<K, T> key, GroupExpression<V, U> value) {

        return new GMap.Mixin<K, V, T, U, Map<T, U>>(key, value, GMap.createLinked(QPair.create(key, value)));
    }

    /**
     * Create a new aggregating map expression using a backing TreeMap
     *
     * @param key key for the map entries
     * @param value value for the map entries
     * @return wrapper expression
     */
    public static <K extends Comparable<? super K>, V> AbstractGroupExpression<Pair<K, V>, SortedMap<K, V>> sortedMap(
            Expression<K> key, Expression<V> value) {

        return GMap.createSorted(QPair.create(key, value));
    }

    /**
     * Create a new aggregating map expression using a backing TreeMap
     *
     * @param key key for the map entries
     * @param value value for the map entries
     * @return wrapper expression
     */
    public static <K, V, T extends Comparable<? super T>> AbstractGroupExpression<Pair<K, V>, SortedMap<T, V>> sortedMap(
            GroupExpression<K, T> key, Expression<V> value) {

        return sortedMap(key, new GOne<V>(value));
    }

    /**
     * Create a new aggregating map expression using a backing TreeMap
     *
     * @param key key for the map entries
     * @param value value for the map entries
     * @return wrapper expression
     */
    public static <K extends Comparable<? super K>, V, U> AbstractGroupExpression<Pair<K, V>, SortedMap<K, U>> sortedMap(
            Expression<K> key, GroupExpression<V, U> value) {

        return sortedMap(new GOne<K>(key), value);
    }

    /**
     * Create a new aggregating map expression using a backing TreeMap
     *
     * @param key key for the map entries
     * @param value value for the map entries
     * @return wrapper expression
     */
    public static <K, V, T extends Comparable<? super T>, U> AbstractGroupExpression<Pair<K, V>, SortedMap<T, U>> sortedMap(
            GroupExpression<K, T> key, GroupExpression<V, U> value) {

        return new GMap.Mixin<K, V, T, U, SortedMap<T, U>>(key, value, GMap.createSorted(QPair.create(key, value)));
    }

    /**
     * Create a new aggregating map expression using a backing TreeMap using the given comparator
     *
     * @param key key for the map entries
     * @param value value for the map entries
     * @param comparator comparator for the created TreeMap instances
     * @return wrapper expression
     */
    public static <K, V> AbstractGroupExpression<Pair<K, V>, SortedMap<K, V>> sortedMap(
            Expression<K> key, Expression<V> value, Comparator<? super K> comparator) {

        return GMap.createSorted(QPair.create(key, value), comparator);
    }

    /**
     * Create a new aggregating map expression using a backing TreeMap using the given comparator
     *
     * @param key key for the map entries
     * @param value value for the map entries
     * @param comparator comparator for the created TreeMap instances
     * @return wrapper expression
     */
    public static <K, V, T> AbstractGroupExpression<Pair<K, V>, SortedMap<T, V>> sortedMap(
            GroupExpression<K, T> key, Expression<V> value, Comparator<? super T> comparator) {

        return sortedMap(key, new GOne<V>(value), comparator);
    }

    /**
     * Create a new aggregating map expression using a backing TreeMap using the given comparator
     *
     * @param key key for the map entries
     * @param value value for the map entries
     * @param comparator comparator for the created TreeMap instances
     * @return wrapper expression
     */
    public static <K, V, U> AbstractGroupExpression<Pair<K, V>, SortedMap<K, U>> sortedMap(
            Expression<K> key, GroupExpression<V, U> value, Comparator<? super K> comparator) {

        return sortedMap(new GOne<K>(key), value, comparator);
    }

    /**
     * Create a new aggregating map expression using a backing TreeMap using the given comparator
     *
     * @param key key for the map entries
     * @param value value for the map entries
     * @param comparator comparator for the created TreeMap instances
     * @return wrapper expression
     */
    public static <K, V, T, U> AbstractGroupExpression<Pair<K, V>, SortedMap<T, U>> sortedMap(
            GroupExpression<K, T> key, GroupExpression<V, U> value, Comparator<? super T> comparator) {

        return new GMap.Mixin<K, V, T, U, SortedMap<T, U>>(key, value, GMap.createSorted(QPair.create(key, value), comparator));
    }

    private GroupBy() { }

}
