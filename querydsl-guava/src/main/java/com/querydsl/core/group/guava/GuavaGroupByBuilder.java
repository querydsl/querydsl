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

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Table;
import com.google.common.collect.Table.Cell;
import com.google.common.collect.TreeBasedTable;
import com.google.common.collect.TreeMultimap;
import com.querydsl.core.ResultTransformer;
import com.querydsl.core.group.Group;
import com.querydsl.core.group.GroupByBuilder;
import com.querydsl.core.types.Expression;

import java.util.Comparator;
import java.util.Map;

/**
 * {@code GroupByBuilder} is a fluent builder for GroupBy transformer instances. This class is not to be used directly,
 * but via {@link GuavaGroupBy}.
 *
 * @param <K>
 * @author Jan-Willem Gmelig Melying
 */
public class GuavaGroupByBuilder<K> extends GroupByBuilder<K> {

    /**
     * Create a new GroupByBuilder for the given key expression
     *
     * @param key key for aggregating
     */
    public GuavaGroupByBuilder(Expression<K> key) {
        super(key);
    }

    /**
     * Get the results as multi map
     *
     * @param expression value expression
     * @param <V> Value type
     * @return new result transformer
     */
    public <V> ResultTransformer<Multimap<K, V>> asMultimap(Expression<V> expression) {
        final Expression<V> lookup = getLookup(expression);
        return new GroupByMultimap<K, V, Multimap<K, V>>(key, expression) {
            @Override
            protected Multimap<K, V> transform(Multimap<K, Group> groups) {
                Multimap<K, V> results = LinkedHashMultimap.create();
                for (Map.Entry<K, Group> entry : groups.entries()) {
                    results.put(entry.getKey(), entry.getValue().getOne(lookup));
                }
                return results;
            }
        };
    }

    /**
     * Get the results as multi map
     *
     * @param expression value expression
     * @param <V> Value type
     * @return new result transformer
     */
    public <V extends Comparable<? super V>> ResultTransformer<TreeMultimap<K, V>> asSortedSetMultimap(Expression<V> expression) {
        final Expression<V> lookup = getLookup(expression);
        return new GroupByMultimap<K, V, TreeMultimap<K, V>>(key, expression) {
            @Override
            protected TreeMultimap<K, V> transform(Multimap<K, Group> groups) {
                TreeMultimap<K, V> results = (TreeMultimap) TreeMultimap.create();
                for (Map.Entry<K, Group> entry : groups.entries()) {
                    results.put(entry.getKey(), entry.getValue().getOne(lookup));
                }
                return results;
            }
        };
    }

    /**
     * Get the results as multi map
     *
     * @param expression value expression
     * @param comparator key comparator
     * @param valueComparator value comparator
     * @param <V> Value type
     * @return new result transformer
     */
    public <V> ResultTransformer<TreeMultimap<K, V>> asSortedSetMultimap(Expression<V> expression,
                                                                         final Comparator<? super K> comparator,
                                                                         final Comparator<? super V> valueComparator) {
        final Expression<V> lookup = getLookup(expression);
        return new GroupByMultimap<K, V, TreeMultimap<K, V>>(key, expression) {
            @Override
            protected TreeMultimap<K, V> transform(Multimap<K, Group> groups) {
                TreeMultimap<K, V> results = TreeMultimap.create(comparator, valueComparator);
                for (Map.Entry<K, Group> entry : groups.entries()) {
                    results.put(entry.getKey(), entry.getValue().getOne(lookup));
                }
                return results;
            }
        };
    }

    /**
     * Get the results as sorted table
     *
     * @param column column expression
     * @param expression value expression
     * @param <C> Column type
     * @param <V> Value type
     * @return new result transformer
     */
    public <C, V> ResultTransformer<Table<K, C, V>> asTable(final Expression<C> column, final Expression<V> expression) {
        final Expression<C> columnKeyLookup = getLookup(column);
        final Expression<V> lookup = getLookup(expression);
        return new GroupByTable<K, C, V, Table<K, C, V>>(key, column, expression) {
            @Override
            protected Table<K, C, V> transform(Table<K, ?, Group> groups) {
                Table<K, C, V> results = HashBasedTable.create();
                for (Cell<K, ?, Group> cell : groups.cellSet()) {
                    K rowKey = cell.getRowKey();
                    C columnKey = cell.getValue().getOne(columnKeyLookup);
                    V value = cell.getValue().getOne(lookup);
                    results.put(rowKey, columnKey, value);
                }
                return results;
            }
        };
    }

    /**
     * Get the results as sorted table
     *
     * @param column column expression
     * @param expression value expression
     * @param <C> Column type
     * @param <V> Value type
     * @return new result transformer
     */
    public <C extends Comparable<? super C>, V> ResultTransformer<TreeBasedTable<K, C, V>> asSortedTable(final Expression<C> column, final Expression<V> expression) {
        final Expression<C> columnKeyLookup = getLookup(column);
        final Expression<V> lookup = getLookup(expression);
        return new GroupByTable<K, C, V, TreeBasedTable<K, C, V>>(key, column, expression) {
            @Override
            protected TreeBasedTable<K, C, V> transform(Table<K, ?, Group> groups) {
                TreeBasedTable<K, C, V> results = (TreeBasedTable) TreeBasedTable.create();
                for (Cell<K, ?, Group> cell : groups.cellSet()) {
                    K rowKey = cell.getRowKey();
                    C columnKey = cell.getValue().getOne(columnKeyLookup);
                    V value = cell.getValue().getOne(lookup);
                    results.put(rowKey, columnKey, value);
                }
                return results;
            }
        };
    }

    /**
     * Get the results as sorted table
     *
     * @param column column expression
     * @param expression value expression
     * @param rowComparator row comparator
     * @param columnComparator column comparator
     * @param <C> Column type
     * @param <V> Value type
     * @return new result transformer
     */
    public <C, V> ResultTransformer<TreeBasedTable<K, C, V>> asSortedTable(final Expression<C> column,
                                                                           final Expression<V> expression,
                                                                           final Comparator<? super K> rowComparator,
                                                                           final Comparator<? super C> columnComparator) {
        final Expression<C> columnKeyLookup = getLookup(column);
        final Expression<V> lookup = getLookup(expression);
        return new GroupByTable<K, C, V, TreeBasedTable<K, C, V>>(key, column, expression) {
            @Override
            protected TreeBasedTable<K, C, V> transform(Table<K, ?, Group> groups) {
                TreeBasedTable<K, C, V> results = TreeBasedTable.create(rowComparator, columnComparator);
                for (Cell<K, ?, Group> cell : groups.cellSet()) {
                    K rowKey = cell.getRowKey();
                    C columnKey = cell.getValue().getOne(columnKeyLookup);
                    V value = cell.getValue().getOne(lookup);
                    results.put(rowKey, columnKey, value);
                }
                return results;
            }
        };
    }

}
