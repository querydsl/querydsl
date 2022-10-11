/*
 * Copyright 2020, The Querydsl Team (http://www.querydsl.com/team)
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
package com.querydsl.core.group.guava;

import com.google.common.collect.Multimap;
import com.google.common.collect.SortedSetMultimap;
import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;
import com.querydsl.core.util.Pair;
import com.querydsl.core.ResultTransformer;
import com.querydsl.core.group.AbstractGroupExpression;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.group.GroupExpression;
import com.querydsl.core.group.QPair;
import com.querydsl.core.types.Expression;

import java.util.Comparator;

/**
 * {@code GuavaGroupBy} extends {@code GroupBy} with factory methods for creating {@link ResultTransformer} and {@link
 * GroupExpression} instances for Guava Collection types.
 *
 * @author Jan-Willem Gmelig Meyling
 */
public final class GuavaGroupBy extends GroupBy {

    /**
     * Create a new GroupByBuilder for the given key expression
     *
     * @param key key for aggregation
     * @return builder for further specification
     */
    public static <K> GuavaGroupByBuilder<K> groupBy(Expression<K> key) {
        return new GuavaGroupByBuilder<K>(key);
    }

    /**
     * Create a new aggregating map expression using a backing LinkedHashMap
     *
     * @param key key for the map entries
     * @param value value for the map entries
     * @return wrapper expression
     */
    public static <K, V> AbstractGroupExpression<Pair<K, V>, Multimap<K, V>> multimap(Expression<K> key,
                                                                                      Expression<V> value) {
        return GMultimap.createLinked(QPair.create(key, value));
    }

    /**
     * Create a new aggregating map expression using a backing LinkedHashMap
     *
     * @param key key for the map entries
     * @param value value for the map entries
     * @return wrapper expression
     */
    public static <K, V, T> AbstractGroupExpression<Pair<K, V>, Multimap<T, V>> multimap(GroupExpression<K, T> key,
                                                                                         Expression<V> value) {
        return multimap(key, new GOne<V>(value));
    }

    /**
     * Create a new aggregating map expression using a backing LinkedHashMap
     *
     * @param key key for the map entries
     * @param value value for the map entries
     * @return wrapper expression
     */
    public static <K, V, U> AbstractGroupExpression<Pair<K, V>, Multimap<K, U>> multimap(Expression<K> key,
                                                                                         GroupExpression<V, U> value) {
        return multimap(new GOne<K>(key), value);
    }

    /**
     * Create a new aggregating map expression using a backing LinkedHashMap
     *
     * @param key key for the map entries
     * @param value value for the map entries
     * @return wrapper expression
     */
    public static <K, V, T, U> AbstractGroupExpression<Pair<K, V>, Multimap<T, U>> multimap(GroupExpression<K, T> key,
                                                                                            GroupExpression<V, U> value) {
        return new GMultimap.Mixin<K, V, T, U, Multimap<T, U>>(key, value, GMultimap.createLinked(QPair.create(key, value)));
    }

    /**
     * Create a new aggregating map expression using a backing TreeMap
     *
     * @param key key for the map entries
     * @param value value for the map entries
     * @return wrapper expression
     */
    public static <K extends Comparable<? super K>, V extends Comparable<? super V>> AbstractGroupExpression<Pair<K, V>, SortedSetMultimap<K, V>> sortedSetMultimap(Expression<K> key,
                                                                                                                                                                    Expression<V> value) {
        return GMultimap.createSorted(QPair.create(key, value));
    }

    /**
     * Create a new aggregating map expression using a backing TreeMap
     *
     * @param key key for the map entries
     * @param value value for the map entries
     * @return wrapper expression
     */
    public static <K, V, T extends Comparable<? super T>> AbstractGroupExpression<Pair<K, V>, SortedSetMultimap<T, V>> sortedSetMultimap(GroupExpression<K, T> key,
                                                                                                                                         Expression<V> value) {
        return sortedSetMultimap(key, (GroupExpression) new GOne<V>(value));
    }

    /**
     * Create a new aggregating map expression using a backing TreeMap
     *
     * @param key key for the map entries
     * @param value value for the map entries
     * @return wrapper expression
     */
    public static <K extends Comparable<? super K>, V, U> AbstractGroupExpression<Pair<K, V>, SortedSetMultimap<K, U>> sortedSetMultimap(Expression<K> key,
                                                                                                                                         GroupExpression<V, U> value) {
        return sortedSetMultimap(new GOne<K>(key), (GroupExpression) value);
    }

    /**
     * Create a new aggregating map expression using a backing TreeMap
     *
     * @param key key for the map entries
     * @param value value for the map entries
     * @return wrapper expression
     */
    public static <K, V, T extends Comparable<? super T>, U extends Comparable<? super U>> AbstractGroupExpression<Pair<K, V>, SortedSetMultimap<T, U>> sortedSetMultimap(GroupExpression<K, T> key,
                                                                                                                                                                          GroupExpression<V, U> value) {
        return new GMultimap.Mixin<K, V, T, U, SortedSetMultimap<T, U>>(key, value, GMultimap.createSorted(QPair.create(key, value)));
    }

    /**
     * Create a new aggregating map expression using a backing TreeMap using the given comparator
     *
     * @param key key for the map entries
     * @param value value for the map entries
     * @param keyComparator comparator for the created TreeMap instances
     * @param valueComparator comparator for the created TreeMap instances
     * @return wrapper expression
     */
    public static <K, V> AbstractGroupExpression<Pair<K, V>, SortedSetMultimap<K, V>> sortedSetMultimap(Expression<K> key,
                                                                                                        Expression<V> value,
                                                                                                        Comparator<? super K> keyComparator,
                                                                                                        Comparator<? super V> valueComparator) {
        return GMultimap.createSorted(QPair.create(key, value), keyComparator, valueComparator);
    }

    /**
     * Create a new aggregating map expression using a backing TreeMap using the given comparator
     *
     * @param key key for the map entries
     * @param value value for the map entries
     * @param comparator comparator for the created TreeMap instances
     * @param valueComparator comparator for the created TreeMap instances
     * @return wrapper expression
     */
    public static <K, V, T> AbstractGroupExpression<Pair<K, V>, SortedSetMultimap<T, V>> sortedSetMultimap(GroupExpression<K, T> key,
                                                                                                           Expression<V> value,
                                                                                                           Comparator<? super T> comparator,
                                                                                                           Comparator<? super V> valueComparator) {
        return sortedSetMultimap(key, new GOne<V>(value), comparator, valueComparator);
    }

    /**
     * Create a new aggregating map expression using a backing TreeMap using the given comparator
     *
     * @param key key for the map entries
     * @param value value for the map entries
     * @param keyComparator comparator for the created TreeMap instances
     * @param valueComparator comparator for the created TreeMap instances
     * @return wrapper expression
     */
    public static <K, V, U> AbstractGroupExpression<Pair<K, V>, SortedSetMultimap<K, U>> sortedSetMultimap(Expression<K> key,
                                                                                                           GroupExpression<V, U> value,
                                                                                                           Comparator<? super K> keyComparator,
                                                                                                           Comparator<? super U> valueComparator) {
        return sortedSetMultimap(new GOne<K>(key), value, keyComparator, valueComparator);
    }

    /**
     * Create a new aggregating map expression using a backing TreeMap using the given comparator
     *
     * @param key key for the map entries
     * @param value value for the map entries
     * @param keyComparator comparator for the created TreeMap instances
     * @param valueComparator comparator for the created TreeMap instances
     * @return wrapper expression
     */
    public static <K, V, T, U> AbstractGroupExpression<Pair<K, V>, SortedSetMultimap<T, U>> sortedSetMultimap(GroupExpression<K, T> key,
                                                                                                              GroupExpression<V, U> value,
                                                                                                              Comparator<? super T> keyComparator,
                                                                                                              Comparator<? super U> valueComparator) {
        return new GMultimap.Mixin<K, V, T, U, SortedSetMultimap<T, U>>(key, value, GMultimap.createSorted(QPair.create(key, value), keyComparator, valueComparator));
    }

    /**
     * Create a new aggregating map expression using a backing LinkedHashMap
     *
     * @param row row for the table entries
     * @param column column for the table entries
     * @param value value for the table entries
     * @return wrapper expression
     */
    public static <R, C, V> AbstractGroupExpression<Pair<Pair<R, C>, V>, Table<R, C, V>> table(Expression<R> row,
                                                                                               Expression<C> column,
                                                                                               Expression<V> value) {
        return GTable.create(QPair.create(QPair.create(row, column), value));
    }

    /**
     * Create a new aggregating map expression using a backing LinkedHashMap
     *
     * @param row row for the table entries
     * @param column column for the table entries
     * @param value value for the table entries
     * @return wrapper expression
     */
    public static <R, C, V, W> AbstractGroupExpression<Pair<Pair<R, C>, V>, Table<W, C, V>> table(GroupExpression<R, W> row,
                                                                                                  Expression<C> column,
                                                                                                  Expression<V> value) {
        return table(row, new GOne<C>(column), new GOne<V>(value));
    }

    /**
     * Create a new aggregating map expression using a backing LinkedHashMap
     *
     * @param row row for the table entries
     * @param column column for the table entries
     * @param value value for the table entries
     * @return wrapper expression
     */
    public static <R, C, V, W, X> AbstractGroupExpression<Pair<Pair<R, C>, V>, Table<W, X, V>> table(GroupExpression<R, W> row,
                                                                                                     GroupExpression<C, X> column,
                                                                                                     Expression<V> value) {
        return table(row, column, new GOne<V>(value));
    }

    /**
     * Create a new aggregating map expression using a backing LinkedHashMap
     *
     * @param row row for the table entries
     * @param column column for the table entries
     * @param value value for the table entries
     * @return wrapper expression
     */
    public static <R, C, V, W> AbstractGroupExpression<Pair<Pair<R, C>, V>, Table<R, W, V>> table(Expression<R> row,
                                                                                                  GroupExpression<C, W> column,
                                                                                                  Expression<V> value) {
        return table(new GOne<R>(row), column, new GOne<V>(value));
    }

    /**
     * Create a new aggregating map expression using a backing LinkedHashMap
     *
     * @param row row for the table entries
     * @param column column for the table entries
     * @param value value for the table entries
     * @return wrapper expression
     */
    public static <R, C, V, W, X> AbstractGroupExpression<Pair<Pair<R, C>, V>, Table<R, X, W>> table(Expression<R> row,
                                                                                                     GroupExpression<C, X> column,
                                                                                                     GroupExpression<V, W> value) {
        return table(new GOne<R>(row), column, value);
    }

    /**
     * Create a new aggregating map expression using a backing LinkedHashMap
     *
     * @param row row for the table entries
     * @param column column for the table entries
     * @param value value for the table entries
     * @return wrapper expression
     */
    public static <R, C, V, W, X> AbstractGroupExpression<Pair<Pair<R, C>, V>, Table<X, C, W>> table(GroupExpression<R, X> row,
                                                                                                     Expression<C> column,
                                                                                                     GroupExpression<V, W> value) {
        return table(row, new GOne<C>(column), value);
    }

    /**
     * Create a new aggregating map expression using a backing LinkedHashMap
     *
     * @param row row for the table entries
     * @param column column for the table entries
     * @param value value for the table entries
     * @return wrapper expression
     */
    public static <R, C, V, W> AbstractGroupExpression<Pair<Pair<R, C>, V>, Table<R, C, W>> table(Expression<R> row,
                                                                                                  Expression<C> column,
                                                                                                  GroupExpression<V, W> value) {
        return table(new GOne<R>(row), new GOne<C>(column), value);
    }


    /**
     * Create a new aggregating map expression using a backing LinkedHashMap
     *
     * @param row row for the table entries
     * @param column column for the table entries
     * @param value value for the table entries
     * @return wrapper expression
     */
    public static <R, C, V, T, U, W> AbstractGroupExpression<Pair<Pair<R, C>, V>, Table<T, U, W>> table(GroupExpression<R, T> row,
                                                                                                        GroupExpression<C, U> column,
                                                                                                        GroupExpression<V, W> value) {
        return new GTable.Mixin<R, C, V, T, U, W, Table<T, U, W>>(
                row, column, value, GTable.create(QPair.create(QPair.create(row, column), value)));
    }

    /**
     * Create a new aggregating map expression using a backing LinkedHashMap
     *
     * @param row row for the table entries
     * @param column column for the table entries
     * @param value value for the table entries
     * @return wrapper expression
     */
    public static <R, C, V> AbstractGroupExpression<Pair<Pair<R, C>, V>, TreeBasedTable<R, C, V>> sortedTable(Expression<R> row,
                                                                                                              Expression<C> column,
                                                                                                              Expression<V> value,
                                                                                                              Comparator<? super R> rowComparator,
                                                                                                              Comparator<? super C> columnComparator) {
        return GTable.createSorted(QPair.create(QPair.create(row, column), value), rowComparator, columnComparator);
    }

    /**
     * Create a new aggregating map expression using a backing LinkedHashMap
     *
     * @param row row for the table entries
     * @param column column for the table entries
     * @param value value for the table entries
     * @return wrapper expression
     */
    public static <R, C, V, W> AbstractGroupExpression<Pair<Pair<R, C>, V>, TreeBasedTable<W, C, V>> sortedTable(GroupExpression<R, W> row,
                                                                                                                 Expression<C> column,
                                                                                                                 Expression<V> value,
                                                                                                                 Comparator<? super W> rowComparator,
                                                                                                                 Comparator<? super C> columnComparator) {
        return GuavaGroupBy.<R, C, V, W, C, V>sortedTable(row, new GOne<C>(column), new GOne<V>(value), rowComparator, columnComparator);
    }

    /**
     * Create a new aggregating map expression using a backing LinkedHashMap
     *
     * @param row row for the table entries
     * @param column column for the table entries
     * @param value value for the table entries
     * @return wrapper expression
     */
    public static <R, C, V, W, X> AbstractGroupExpression<Pair<Pair<R, C>, V>, TreeBasedTable<W, X, V>> sortedTable(GroupExpression<R, W> row,
                                                                                                                    GroupExpression<C, X> column,
                                                                                                                    Expression<V> value,
                                                                                                                    Comparator<? super W> rowComparator,
                                                                                                                    Comparator<? super X> columnComparator) {
        return GuavaGroupBy.<R, C, V, W, X, V> sortedTable(row, column, new GOne<V>(value), rowComparator, columnComparator);
    }

    /**
     * Create a new aggregating map expression using a backing LinkedHashMap
     *
     * @param row row for the table entries
     * @param column column for the table entries
     * @param value value for the table entries
     * @return wrapper expression
     */
    public static <R, C, V, W> AbstractGroupExpression<Pair<Pair<R, C>, V>, TreeBasedTable<R, W, V>> sortedTable(Expression<R> row,
                                                                                                                 GroupExpression<C, W> column,
                                                                                                                 Expression<V> value,
                                                                                                                 Comparator<? super R> rowComparator,
                                                                                                                 Comparator<? super W> columnComparator) {
        return GuavaGroupBy.<R, C, V, R, W, V>sortedTable(new GOne<R>(row), column, new GOne<V>(value), rowComparator, columnComparator);
    }

    /**
     * Create a new aggregating map expression using a backing LinkedHashMap
     *
     * @param row row for the table entries
     * @param column column for the table entries
     * @param value value for the table entries
     * @return wrapper expression
     */
    public static <R, C, V, W, X> AbstractGroupExpression<Pair<Pair<R, C>, V>, TreeBasedTable<R, X, W>> sortedTable(Expression<R> row,
                                                                                                                    GroupExpression<C, X> column,
                                                                                                                    GroupExpression<V, W> value,
                                                                                                                    Comparator<? super R> rowComparator,
                                                                                                                    Comparator<? super X> columnComparator) {
        return GuavaGroupBy.<R, C, V, R, X, W>sortedTable(new GOne<R>(row), column, value, rowComparator, columnComparator);
    }

    /**
     * Create a new aggregating map expression using a backing LinkedHashMap
     *
     * @param row row for the table entries
     * @param column column for the table entries
     * @param value value for the table entries
     * @return wrapper expression
     */
    public static <R, C, V, W, X> AbstractGroupExpression<Pair<Pair<R, C>, V>, TreeBasedTable<X, C, W>> sortedTable(GroupExpression<R, X> row,
                                                                                                                    Expression<C> column,
                                                                                                                    GroupExpression<V, W> value,
                                                                                                                    Comparator<? super X> rowComparator,
                                                                                                                    Comparator<? super C> columnComparator) {
        return sortedTable(row, new GOne<C>(column), value, rowComparator, columnComparator);
    }

    /**
     * Create a new aggregating map expression using a backing LinkedHashMap
     *
     * @param row row for the table entries
     * @param column column for the table entries
     * @param value value for the table entries
     * @return wrapper expression
     */
    public static <R, C, V, W> AbstractGroupExpression<Pair<Pair<R, C>, V>, TreeBasedTable<R, C, W>> sortedTable(Expression<R> row,
                                                                                                                 Expression<C> column,
                                                                                                                 GroupExpression<V, W> value,
                                                                                                                 Comparator<? super R> rowComparator,
                                                                                                                 Comparator<? super C> columnComparator) {
        return GuavaGroupBy.<R, C, V, R, C, W>sortedTable(new GOne<R>(row), new GOne<C>(column), value, rowComparator, columnComparator);
    }


    /**
     * Create a new aggregating map expression using a backing LinkedHashMap
     *
     * @param row row for the table entries
     * @param column column for the table entries
     * @param value value for the table entries
     * @return wrapper expression
     */
    public static <R, C, V, T, U, W> AbstractGroupExpression<Pair<Pair<R, C>, V>, TreeBasedTable<T, U, W>> sortedTable(GroupExpression<R, T> row,
                                                                                                                       GroupExpression<C, U> column,
                                                                                                                       GroupExpression<V, W> value,
                                                                                                                       Comparator<? super T> rowComparator,
                                                                                                                       Comparator<? super U> columnComparator) {
        return new GTable.Mixin<R, C, V, T, U, W, TreeBasedTable<T, U, W>>(
                row, column, value, GTable.createSorted(QPair.create(QPair.create(row, column), value), rowComparator, columnComparator));
    }

    private GuavaGroupBy() {
    }

}
