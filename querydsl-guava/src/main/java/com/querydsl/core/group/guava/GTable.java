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
import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;
import com.querydsl.core.util.Pair;
import com.querydsl.core.group.AbstractGroupExpression;
import com.querydsl.core.group.GroupCollector;
import com.querydsl.core.group.GroupExpression;
import com.querydsl.core.group.QPair;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

abstract class GTable<R, C, V, M extends Table<R, C, V>> extends AbstractGroupExpression<Pair<Pair<R, C>, V>, M> {

    private static final long serialVersionUID = 7106389414200843920L;

    GTable(QPair<Pair<R,C>, V> qpair) {
        super(Table.class, qpair);
    }

    protected abstract M createTable();

    public static <T, U, W> GTable<T, U, W, Table<T, U, W>> create(QPair<Pair<T, U>, W> expr) {
        return new GTable<T, U, W, Table<T, U, W>>(expr) {
            @Override
            protected Table<T, U, W> createTable() {
                return HashBasedTable.create();
            }
        };
    }

    public static <T extends Comparable<? super T>, U extends Comparable<? super U>, W> GTable<T, U, W, TreeBasedTable<T, U, W>> createSorted(QPair<Pair<T, U>, W> expr) {
        return new GTable<T, U, W, TreeBasedTable<T, U, W>>(expr) {
            @Override
            protected TreeBasedTable<T, U, W> createTable() {
                return TreeBasedTable.create();
            }
        };
    }

    public static <T, U, W> GTable<T, U, W, TreeBasedTable<T, U, W>> createSorted(QPair<Pair<T, U>, W> expr, final Comparator<? super T> rowComparator, final Comparator<? super U> columnComparator) {
        return new GTable<T, U, W, TreeBasedTable<T, U, W>>(expr) {
            @Override
            protected TreeBasedTable<T, U, W> createTable() {
                return TreeBasedTable.create(rowComparator, columnComparator);
            }
        };
    }

    @Override
    public GroupCollector<Pair<Pair<R, C>, V>, M> createGroupCollector() {
        return new GroupCollector<Pair<Pair<R, C>, V>, M>() {

            private final M table = createTable();

            @Override
            public void add(Pair<Pair<R, C>, V> pair) {
                table.put(pair.getFirst().getFirst(), pair.getFirst().getSecond(), pair.getSecond());
            }

            @Override
            public M get() {
                return table;
            }

        };
    }

    static class Mixin<R, C, V, T, U, W, RES extends Table<? super T, ? super U, ? super W>> extends AbstractGroupExpression<Pair<Pair<R, C>, V>, RES> {

        private static final long serialVersionUID = 1939989270493531116L;

        private class GroupCollectorImpl implements GroupCollector<Pair<Pair<R, C>, V>, RES> {

            private final  GroupCollector<Pair<Pair<T, U>, W>, RES> groupCollector;

            private final Table<R, C, GroupCollector<R, T>> rowCollectors = HashBasedTable.create();
            private final Map<GroupCollector<R, T>, GroupCollector<C, U>> columnCollectors = new HashMap<GroupCollector<R, T>, GroupCollector<C, U>>();
            private final Map<GroupCollector<C, U>, GroupCollector<V, W>> valueCollectors = new HashMap<GroupCollector<C, U>, GroupCollector<V, W>>();

            GroupCollectorImpl() {
                this.groupCollector = mixin.createGroupCollector();
            }

            @Override
            public void add(Pair<Pair<R, C>, V> pair) {
                Pair<R, C> first = pair.getFirst();
                R rowKey = first.getFirst();
                C columnKey = first.getSecond();

                GroupCollector<R, T> rowCollector = rowCollectors.get(rowKey, columnKey);
                if (rowCollector == null) {
                    rowCollector = rowExpression.createGroupCollector();
                    rowCollectors.put(rowKey, columnKey, rowCollector);
                }
                rowCollector.add(rowKey);

                GroupCollector<C, U> columnCollector = columnCollectors.get(rowCollector);
                if (columnCollector == null) {
                    columnCollector = columnExpression.createGroupCollector();
                    columnCollectors.put(rowCollector, columnCollector);
                }
                columnCollector.add(columnKey);

                GroupCollector<V, W> valueCollector = valueCollectors.get(columnCollector);
                if (valueCollector == null) {
                    valueCollector = valueExpression.createGroupCollector();
                    valueCollectors.put(columnCollector, valueCollector);
                }
                V second = pair.getSecond();
                valueCollector.add(second);
            }

            @Override
            public RES get() {
                for (GroupCollector<R, T> rowCollector : rowCollectors.values()) {
                    T rowKey = rowCollector.get();
                    GroupCollector<C, U> columnCollector = columnCollectors.get(rowCollector);
                    U columnKey = columnCollector.get();
                    GroupCollector<V, W> valueCollector = valueCollectors.get(columnCollector);
                    W value = valueCollector.get();
                    groupCollector.add(Pair.of(Pair.of(rowKey, columnKey), value));
                }
                return groupCollector.get();
            }

        }

        private final GroupExpression<Pair<Pair<T, U>, W>, RES> mixin;
        private final GroupExpression<R, T> rowExpression;
        private final GroupExpression<C, U> columnExpression;
        private final GroupExpression<V, W> valueExpression;

        @SuppressWarnings({ "rawtypes", "unchecked" })
        Mixin(GroupExpression<R, T> rowExpression, GroupExpression<C, U> columnExpression, GroupExpression<V, W> valueExpression, AbstractGroupExpression<Pair<Pair<T, U>, W>, RES> mixin) {
            super((Class) mixin.getType(), QPair.create(QPair.create(rowExpression.getExpression(), columnExpression.getExpression()), valueExpression.getExpression()));
            this.rowExpression = rowExpression;
            this.columnExpression = columnExpression;
            this.valueExpression = valueExpression;
            this.mixin = mixin;
        }

        @Override
        public GroupCollector<Pair<Pair<R, C>, V>, RES> createGroupCollector() {
            return new GroupCollectorImpl();
        }
    }

}