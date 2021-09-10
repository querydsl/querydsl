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

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.SortedSetMultimap;
import com.google.common.collect.TreeMultimap;
import com.querydsl.core.group.AbstractGroupExpression;
import com.querydsl.core.group.GroupCollector;
import com.querydsl.core.group.GroupExpression;
import com.querydsl.core.group.QPair;
import com.mysema.commons.lang.Pair;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

abstract class GMultimap<K, V, M extends Multimap<K,V>> extends AbstractGroupExpression<Pair<K, V>, M> {

    private static final long serialVersionUID = 7106389414200843920L;

    GMultimap(QPair<K,V> qpair) {
        super(Multimap.class, qpair);
    }

    protected abstract M createMap();

    public static <T, U> GMultimap<T, U, Multimap<T,U>> createLinked(QPair<T, U> expr) {
        return new GMultimap<T, U, Multimap<T, U>>(expr) {
            @Override
            protected Multimap<T, U> createMap() {
                return LinkedHashMultimap.create();
            }
        };
    }

    public static <T extends Comparable<? super T>, U extends Comparable<? super U>> GMultimap<T, U, SortedSetMultimap<T, U>> createSorted(QPair<T, U> expr) {
        return new GMultimap<T, U, SortedSetMultimap<T, U>>(expr) {
            @Override
            protected SortedSetMultimap<T, U> createMap() {
                return TreeMultimap.create();
            }
        };
    }

    public static <T, U> GMultimap<T, U, SortedSetMultimap<T, U>> createSorted(QPair<T, U> expr, final Comparator<? super T> comparator, final Comparator<? super U> comparator2) {
        return new GMultimap<T, U, SortedSetMultimap<T, U>>(expr) {
            @Override
            protected SortedSetMultimap<T, U> createMap() {
                return TreeMultimap.create(comparator, comparator2);
            }
        };
    }

    @Override
    public GroupCollector<Pair<K,V>, M> createGroupCollector() {
        return new GroupCollector<Pair<K,V>, M>() {

            private final M map = createMap();

            @Override
            public void add(Pair<K,V> pair) {
                map.put(pair.getFirst(), pair.getSecond());
            }

            @Override
            public M get() {
                return map;
            }

        };
    }

    static class Mixin<K, V, T, U, R extends Multimap<? super T, ? super U>> extends AbstractGroupExpression<Pair<K, V>, R> {

        private static final long serialVersionUID = 1939989270493531116L;

        private class GroupCollectorImpl implements GroupCollector<Pair<K, V>, R> {

            private final GroupCollector<Pair<T, U>, R> groupCollector;

            private final Map<K, GroupCollector<K, T>> keyCollectors = new LinkedHashMap<K, GroupCollector<K, T>>();

            private final Map<GroupCollector<K, T>, GroupCollector<V, U>> valueCollectors = new HashMap<GroupCollector<K, T>, GroupCollector<V, U>>();

            GroupCollectorImpl() {
                this.groupCollector = mixin.createGroupCollector();
            }

            @Override
            public void add(Pair<K, V> pair) {
                K first = pair.getFirst();
                GroupCollector<K, T> keyCollector = keyCollectors.get(first);
                if (keyCollector == null) {
                    keyCollector = keyExpression.createGroupCollector();
                    keyCollectors.put(first, keyCollector);
                }
                keyCollector.add(first);
                GroupCollector<V, U> valueCollector = valueCollectors.get(keyCollector);
                if (valueCollector == null) {
                    valueCollector = valueExpression.createGroupCollector();
                    valueCollectors.put(keyCollector, valueCollector);
                }
                V second = pair.getSecond();
                valueCollector.add(second);
            }

            @Override
            public R get() {
                for (GroupCollector<K, T> keyCollector : keyCollectors.values()) {
                    T key = keyCollector.get();
                    GroupCollector<V, U> valueCollector = valueCollectors.remove(keyCollector);
                    U value = valueCollector.get();
                    groupCollector.add(Pair.of(key, value));
                }
                keyCollectors.clear();
                return groupCollector.get();
            }

        }

        private final GroupExpression<Pair<T, U>, R> mixin;

        private final GroupExpression<K, T> keyExpression;
        private final GroupExpression<V, U> valueExpression;

        @SuppressWarnings({ "rawtypes", "unchecked" })
        Mixin(GroupExpression<K, T> keyExpression, GroupExpression<V, U> valueExpression, AbstractGroupExpression<Pair<T, U>, R> mixin) {
            super((Class) mixin.getType(), QPair.create(keyExpression.getExpression(), valueExpression.getExpression()));
            this.keyExpression = keyExpression;
            this.valueExpression = valueExpression;
            this.mixin = mixin;
        }

        @Override
        public GroupCollector<Pair<K, V>, R> createGroupCollector() {
            return new GroupCollectorImpl();
        }

    }

}