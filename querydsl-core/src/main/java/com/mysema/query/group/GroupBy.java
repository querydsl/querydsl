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
package com.mysema.query.group;

import java.util.*;

import com.infradna.tool.bridge_method_injector.WithBridgeMethods;
import com.mysema.commons.lang.Pair;
import com.mysema.query.types.Expression;
import com.mysema.query.types.QList;

/**
 * Groups results by the first expression.
 *
 * @author sasa
 * @author tiwe
 *
 */
public final class GroupBy {

    /**
     * Create a new GroupByBuilder for the given key expression
     *
     * @param key
     * @return
     */
    public static <K> GroupByBuilder<K> groupBy(Expression<K> key) {
        return new GroupByBuilder<K>(key);
    }

    /**
     * Create a new GroupByBuilder for the given key expressions
     *
     * @param keys
     * @return
     */
    public static GroupByBuilder<List<?>> groupBy(Expression<?>... keys) {
        return new GroupByBuilder<List<?>>(new QList(keys));
    }

    /**
     * Create a new aggregating min expression
     *
     * @param expression
     * @return
     */
    public static <E extends Comparable<? super E>> AbstractGroupExpression<E, E> min(Expression<E> expression) {
        return new GMin<E>(expression);
    }

    /**
     * Create a new aggregating sum expression
     *
     * @param expression
     * @return
     */
    public static <E extends Number> AbstractGroupExpression<E, E> sum(Expression<E> expression) {
        return new GSum<E>(expression);
    }

    /**
     * Create a new aggregating avg expression
     *
     * @param expression
     * @return
     */
    public static <E extends Number> AbstractGroupExpression<E, E> avg(Expression<E> expression) {
        return new GAvg<E>(expression);
    }

    /**
     * Create a new aggregating max expression
     *
     * @param expression
     * @return
     */
    public static <E extends Comparable<? super E>> AbstractGroupExpression<E, E> max(Expression<E> expression) {
        return new GMax<E>(expression);
    }

    /**
     * Create a new aggregating list expression
     *
     * @param expression
     * @return
     */
    public static <E> AbstractGroupExpression<E, List<E>> list(Expression<E> expression) {
        return new GList<E>(expression);
    }

    public static <E, F> AbstractGroupExpression<E, List<F>> list(GroupExpression<E, F> groupExpression) {
        return new MixinGroupExpression<E, F, List<F>>(groupExpression, new GList<F>(groupExpression));
    }

    /**
     * Create a new aggregating set expression using a backing LinkedHashSet
     *
     * @param expression
     * @return
     */
    public static <E> AbstractGroupExpression<E, Set<E>> set(Expression<E> expression) {
        return GSet.createLinked(expression);
    }

    public static <E, F> GroupExpression<E, Set<F>> set(GroupExpression<E, F> groupExpression) {
        return new MixinGroupExpression<E, F, Set<F>>(groupExpression, GSet.createLinked(groupExpression));
    }

    /**
     * Create a new aggregating set expression using a backing TreeSet
     *
     * @param expression
     * @return
     */
    public static <E extends Comparable<? super E>> AbstractGroupExpression<E, SortedSet<E>> sortedSet(Expression<E> expression) {
        return GSet.createSorted(expression);
    }

    /**
     * Create a new aggregating set expression using a backing TreeSet
     *
     * @param groupExpression
     * @return
     */
    public static <E, F extends Comparable<? super F>> GroupExpression<E, SortedSet<F>> sortedSet(GroupExpression<E, F> groupExpression) {
        return new MixinGroupExpression<E, F, SortedSet<F>>(groupExpression, GSet.createSorted(groupExpression));
    }


    /**
     * Create a new aggregating set expression using a backing TreeSet using the given comparator
     *
     * @param expression
     * @param comparator
     * @return
     */
    public static <E> AbstractGroupExpression<E, SortedSet<E>> sortedSet(Expression<E> expression, Comparator<? super E> comparator) {
        return GSet.createSorted(expression, comparator);
    }

    /**
     * Create a new aggregating set expression using a backing TreeSet using the given comparator
     *
     * @param groupExpression
     * @param comparator
     * @return
     */
    public static <E, F> GroupExpression<E, SortedSet<F>> sortedSet(GroupExpression<E, F> groupExpression, Comparator<? super F> comparator) {
        return new MixinGroupExpression<E, F, SortedSet<F>>(groupExpression, GSet.createSorted(groupExpression, comparator));
    }


    /**
     * Create a new aggregating map expression using a backing LinkedHashMap
     *
     * @param key
     * @param value
     * @return
     */
    @WithBridgeMethods(value=Expression.class,castRequired=true)
    public static <K, V> AbstractGroupExpression<Pair<K, V>,Map<K, V>> map(Expression<K> key, Expression<V> value) {
        return GMap.createLinked(QPair.create(key, value));
    }

    /**
     * Create a new aggregating map expression using a backing LinkedHashMap
     *
     * @param key
     * @param value
     * @return
     */
    public static <K, V, T> AbstractGroupExpression<Pair<K, V>, Map<T, V>> map(GroupExpression<K, T> key, Expression<V> value) {
        return map(key, new GOne<V>(value));
    }

    /**
     * Create a new aggregating map expression using a backing LinkedHashMap
     *
     * @param key
     * @param value
     * @return
     */
    public static <K, V, U> AbstractGroupExpression<Pair<K, V>, Map<K, U>> map(Expression<K> key, GroupExpression<V, U> value) {
        return map(new GOne<K>(key), value);
    }

    /**
     * Create a new aggregating map expression using a backing LinkedHashMap
     *
     * @param key
     * @param value
     * @return
     */
    public static <K, V, T, U> AbstractGroupExpression<Pair<K, V>, Map<T, U>> map(GroupExpression<K, T> key, GroupExpression<V, U> value) {
        return new GMap.Mixin<K, V, T, U, Map<T, U>>(key, value, GMap.createLinked(QPair.create(key, value)));
    }

    /**
     * Create a new aggregating map expression using a backing TreeMap
     *
     * @param key
     * @param value
     * @return
     */
    public static <K extends Comparable<? super K>, V> AbstractGroupExpression<Pair<K, V>, SortedMap<K, V>> sortedMap(Expression<K> key, Expression<V> value) {
        return GMap.createSorted(QPair.create(key, value));
    }

    /**
     * Create a new aggregating map expression using a backing TreeMap
     *
     * @param key
     * @param value
     * @return
     */
    public static <K extends Comparable<? super K>, V, T extends Comparable<? super T>> AbstractGroupExpression<Pair<K, V>, SortedMap<T, V>> sortedMap(GroupExpression<K, T> key, Expression<V> value) {
        return sortedMap(key, new GOne<V>(value));
    }

    /**
     * Create a new aggregating map expression using a backing TreeMap
     *
     * @param key
     * @param value
     * @return
     */
    public static <K extends Comparable<? super K>, V, U> AbstractGroupExpression<Pair<K, V>, SortedMap<K, U>> sortedMap(Expression<K> key, GroupExpression<V, U> value) {
        return sortedMap(new GOne<K>(key), value);
    }

    /**
     * Create a new aggregating map expression using a backing TreeMap
     *
     * @param key
     * @param value
     * @return
     */
    public static <K extends Comparable<? super K>, V, T extends Comparable<? super T>, U> AbstractGroupExpression<Pair<K, V>, SortedMap<T, U>> sortedMap(GroupExpression<K, T> key, GroupExpression<V, U> value) {
        return new GMap.Mixin<K, V, T, U, SortedMap<T, U>>(key, value, GMap.createSorted(QPair.create(key, value)));
    }

    /**
     * Create a new aggregating map expression using a backing TreeMap using the given comparator
     *
     * @param key
     * @param value
     * @param comparator
     * @return
     */
    public static <K, V> AbstractGroupExpression<Pair<K, V>, SortedMap<K, V>> sortedMap(Expression<K> key, Expression<V> value, Comparator<? super K> comparator) {
        return GMap.createSorted(QPair.create(key, value), comparator);
    }

    /**
     * Create a new aggregating map expression using a backing TreeMap using the given comparator
     *
     * @param key
     * @param value
     * @param comparator
     * @return
     */
    public static <K, V, T> AbstractGroupExpression<Pair<K, V>, SortedMap<T, V>> sortedMap(GroupExpression<K, T> key, Expression<V> value, Comparator<? super K> comparator) {
        return sortedMap(key, new GOne<V>(value), comparator);
    }

    /**
     * Create a new aggregating map expression using a backing TreeMap using the given comparator
     *
     * @param key
     * @param value
     * @param comparator
     * @return
     */
    public static <K, V, U> AbstractGroupExpression<Pair<K, V>, SortedMap<K, U>> sortedMap(Expression<K> key, GroupExpression<V, U> value, Comparator<? super U> comparator) {
        return sortedMap(new GOne<K>(key), value, comparator);
    }

    /**
     * Create a new aggregating map expression using a backing TreeMap using the given comparator
     *
     * @param key
     * @param value
     * @param comparator
     * @return
     */
    public static <K, V, T, U> AbstractGroupExpression<Pair<K, V>, SortedMap<T, U>> sortedMap(GroupExpression<K, T> key, GroupExpression<V, U> value, Comparator<? super T> comparator) {
        return new GMap.Mixin<K, V, T, U, SortedMap<T, U>>(key, value, GMap.createSorted(QPair.create(key, value), comparator));
    }


    private GroupBy() {}

}
