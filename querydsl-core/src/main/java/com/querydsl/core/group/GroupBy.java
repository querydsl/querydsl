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
package com.querydsl.core.group;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.infradna.tool.bridge_method_injector.WithBridgeMethods;
import com.mysema.commons.lang.Pair;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.QList;

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
    public static <E extends Comparable<E>> AbstractGroupExpression<E, E> min(Expression<E> expression) {
        return new GMin<E>(expression);
    }

    /**
     * Create a new aggregating sum expression
     *
     * @param expression
     * @return
     */
    public static <E extends Number & Comparable<E>> AbstractGroupExpression<E, E> sum(Expression<E> expression) {
        return new GSum<E>(expression);
    }

    /**
     * Create a new aggregating avg expression
     *
     * @param expression
     * @return
     */
    public static <E extends Number & Comparable<E>> AbstractGroupExpression<E, E> avg(Expression<E> expression) {
        return new GAvg<E>(expression);
    }

    /**
     * Create a new aggregating max expression
     *
     * @param expression
     * @return
     */
    public static <E extends Comparable<E>> AbstractGroupExpression<E, E> max(Expression<E> expression) {
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
     * Create a new aggregating set expression
     *
     * @param expression
     * @return
     */
    public static <E> AbstractGroupExpression<E, Set<E>> set(Expression<E> expression) {
        return new GSet<E>(expression);
    }

    public static <E, F> GroupExpression<E, Set<F>> set(GroupExpression<E, F> groupExpression) {
        return new MixinGroupExpression<E, F, Set<F>>(groupExpression, new GSet<F>(groupExpression));
    }

    /**
     * Create a new aggregating map expression
     *
     * @param key
     * @param value
     * @return
     */
    @WithBridgeMethods(value=Expression.class,castRequired=true)
    public static <K, V> AbstractGroupExpression<Pair<K, V>,Map<K, V>> map(Expression<K> key, Expression<V> value) {
        return new GMap<K, V>(QPair.create(key, value));
    }

    public static <K, V, T> AbstractGroupExpression<Pair<K, V>, Map<T, V>> map(GroupExpression<K, T> key, Expression<V> value) {
        return map(key, new GOne<V>(value));
    }

    public static <K, V, U> AbstractGroupExpression<Pair<K, V>, Map<K, U>> map(Expression<K> key, GroupExpression<V, U> value) {
        return map(new GOne<K>(key), value);
    }

    public static <K, V, T, U> AbstractGroupExpression<Pair<K, V>, Map<T, U>> map(GroupExpression<K, T> key, GroupExpression<V, U> value) {
        return new GMap.Mixin<K, V, T, U, Map<T, U>>(key, value, new GMap<T, U>(QPair.create(key, value)));
    }

    private GroupBy() {}

}
