/*
 * Copyright 2012, Mysema Ltd
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
package com.querydsl.core.util;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * CollectionUtils provides addition operations for Collection types that provide an immutable type
 * for single item collections and after that mutable instances
 *
 * @author tiwe
 *
 */
public final class CollectionUtils {

    public static <T> List<T> add(List<T> list, T element) {
        final int size = list.size();
        if (size == 0) {
            return ImmutableList.of(element);
        } else if (list instanceof ImmutableList) {
            if (size == 1) {
                final T val = list.get(0);
                list = Lists.newArrayList();
                list.add(val);
            } else {
                list = Lists.newArrayList(list);
            }
        }
        list.add(element);
        return list;
    }

    public static <T> List<T> copyOf(List<T> list) {
        if (list instanceof ImmutableList) {
            return list;
        } else {
            return Lists.newArrayList(list);
        }
    }

    public static <T> Set<T> add(Set<T> set, T element) {
        final int size = set.size();
        if (size == 0) {
            return ImmutableSet.of(element);
        } else if (set instanceof ImmutableSet) {
            if (size == 1) {
                final T val = set.iterator().next();
                set = Sets.newHashSet();
                set.add(val);
            } else {
                set = Sets.newHashSet(set);
            }
        }
        set.add(element);
        return set;
    }

    public static <T> Set<T> copyOf(Set<T> set) {
        if (set instanceof ImmutableSet) {
            return set;
        } else {
            return Sets.newHashSet(set);
        }
    }

    public static <T> Set<T> addSorted(Set<T> set, T element) {
        final int size = set.size();
        if (size == 0) {
            return ImmutableSet.of(element);
        } else if (set instanceof ImmutableSet) {
            if (size == 1) {
                final T val = set.iterator().next();
                set = Sets.newLinkedHashSet();
                set.add(val);
            } else {
                set = Sets.newLinkedHashSet(set);
            }
        }
        set.add(element);
        return set;
    }

    public static <T> Set<T> removeSorted(Set<T> set, T element) {
        final int size = set.size();
        if (size == 0 || (size == 1 && set.contains(element))) {
            return ImmutableSet.of();
        } else {
            set.remove(element);
        }
        return set;
    }

    public static <T> Set<T> copyOfSorted(Set<T> set) {
        if (set instanceof ImmutableSet) {
            return set;
        } else {
            return Sets.newLinkedHashSet(set);
        }
    }

    public static <K,V> Map<K,V> put(Map<K,V> map, K key, V value) {
        final int size = map.size();
        if (size == 0) {
            return ImmutableMap.of(key, value);
        } else if (map instanceof ImmutableMap) {
            map = Maps.newHashMap(map);
        }
        map.put(key, value);
        return map;
    }

    public static <K,V> Map<K,V> copyOf(Map<K,V> map) {
        if (map instanceof ImmutableMap) {
            return map;
        } else {
            return Maps.newHashMap(map);
        }
    }

    private CollectionUtils() {}

}
