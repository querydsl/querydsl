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
package com.mysema.query.group;

import java.util.List;
import java.util.Map;
import java.util.Set;

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
    public static <E extends Comparable<E>> AbstractGroupExpression<?,E> min(Expression<E> expression) {
        return new GMin<E>(expression);
    }

    /**
     * Create a new aggregating sum expression
     *
     * @param expression
     * @return
     */
    public static <E extends Number & Comparable<E>> AbstractGroupExpression<?,E> sum(Expression<E> expression) {
        return new GSum<E>(expression);
    }

    /**
     * Create a new aggregating avg expression
     *
     * @param expression
     * @return
     */
    public static <E extends Number & Comparable<E>> AbstractGroupExpression<?,E> avg(Expression<E> expression) {
        return new GAvg<E>(expression);
    }

    /**
     * Create a new aggregating max expression
     *
     * @param expression
     * @return
     */
    public static <E extends Comparable<E>> AbstractGroupExpression<?,E> max(Expression<E> expression) {
        return new GMax<E>(expression);
    }

    /**
     * Create a new aggregating list expression
     *
     * @param expression
     * @return
     */
    public static <E> AbstractGroupExpression<?,List<E>> list(Expression<E> expression) {
        return new GList<E>(expression);
    }

    /**
     * Create a new aggregating set expression
     *
     * @param expression
     * @return
     */
    public static <E> AbstractGroupExpression<?,Set<E>> set(Expression<E> expression) {
        return new GSet<E>(expression);
    }

    /**
     * Create a new aggregating map expression
     *
     * @param key
     * @param value
     * @return
     */
    public static <K, V> Expression<Map<K, V>> map(Expression<K> key, Expression<V> value) {
        QPair<K,V> qPair = new QPair<K,V>(key, value);
        return new GMap<K,V>(qPair);
    }

    private GroupBy() {}

}
