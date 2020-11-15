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
package com.querydsl.core.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.google.common.collect.*;

/**
 * CollectionUtils provides addition operations for Collection types that provide an immutable type
 * for single item collections and after that mutable instances
 *
 * @author tiwe
 *
 */
public final class CollectionUtils {

    private static final Set<Class<?>> UNMODIFIABLE_TYPES;

    static {
        Set<Class<?>> unmodifiableTypes = new HashSet<>();
        unmodifiableTypes.add(Collections.emptyList().getClass());
        unmodifiableTypes.add(Collections.emptySet().getClass());
        unmodifiableTypes.add(Collections.emptyNavigableSet().getClass());
        unmodifiableTypes.add(Collections.emptySortedSet().getClass());
        unmodifiableTypes.add(Collections.emptyMap().getClass());
        unmodifiableTypes.add(Collections.emptySortedMap().getClass());
        unmodifiableTypes.add(Collections.emptyNavigableMap().getClass());
        unmodifiableTypes.add(Collections.singleton(1).getClass());
        unmodifiableTypes.add(Collections.singletonList(1).getClass());
        unmodifiableTypes.add(Collections.singletonMap(1, 1).getClass());
        unmodifiableTypes.add(Collections.unmodifiableList(Collections.emptyList()).getClass());
        unmodifiableTypes.add(Collections.unmodifiableCollection(Collections.emptyList()).getClass());
        unmodifiableTypes.add(Collections.unmodifiableSet(Collections.emptySet()).getClass());
        unmodifiableTypes.add(Collections.unmodifiableNavigableSet(Collections.emptyNavigableSet()).getClass());
        unmodifiableTypes.add(Collections.unmodifiableSortedSet(Collections.emptySortedSet()).getClass());
        unmodifiableTypes.add(Collections.unmodifiableMap(Collections.emptyMap()).getClass());
        unmodifiableTypes.add(Collections.unmodifiableSortedMap(Collections.emptySortedMap()).getClass());
        unmodifiableTypes.add(Collections.unmodifiableNavigableMap(Collections.emptyNavigableMap()).getClass());

        try {
            unmodifiableTypes.add(Class.forName("com.google.common.collect.ImmutableSet"));
            unmodifiableTypes.add(Class.forName("com.google.common.collect.ImmutableList"));
            unmodifiableTypes.add(Class.forName("com.google.common.collect.ImmutableMap"));
        } catch (ClassNotFoundException e) {
            // Nothing happens
        }

        try {
            unmodifiableTypes.add(Class.forName("java.util.ImmutableCollections$AbstractImmutableCollection"));
            unmodifiableTypes.add(Class.forName("java.util.ImmutableCollections$AbstractImmutableMap"));
        } catch (ClassNotFoundException e) {
            // Nothing happens
        }

        UNMODIFIABLE_TYPES = Collections.unmodifiableSet(unmodifiableTypes);
    }

    /**
     * Returns true if the type is a known unmodifiable type.
     *
     * @param clazz the type
     * @return true if the type is a known unmodifiable type
     */
    public static boolean isUnmodifiableType(Class<?> clazz) {
        for (; clazz != null; clazz = clazz.getSuperclass()) {
            if (UNMODIFIABLE_TYPES.contains(clazz)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Return an unmodifiable copy of a list, or the same list if its already an unmodifiable type.
     *
     * @param list the list
     * @param <T> element type
     * @return unmodifiable copy of a list, or the same list if its already an unmodifiable type
     */
    public static <T> List<T> unmodifiableList(List<T> list) {
        if (isUnmodifiableType(list.getClass())) {
            return list;
        }
        switch (list.size()) {
            case 0:
                return Collections.emptyList();
            case 1:
                return Collections.singletonList(list.get(0));
            default:
                return Collections.unmodifiableList(new ArrayList<>(list));
        }
    }

    /**
     * Return an unmodifiable copy of a set, or the same set if its already an unmodifiable type.
     *
     * @param set the set
     * @param <T> element type
     * @return unmodifiable copy of a set, or the same set if its already an unmodifiable type
     */
    public static <T> Set<T> unmodifiableSet(Set<T> set) {
        if (isUnmodifiableType(set.getClass())) {
            return set;
        }
        switch (set.size()) {
            case 0:
                return Collections.emptySet();
            case 1:
                return Collections.singleton(set.iterator().next());
            default:
                return Collections.unmodifiableSet(new HashSet<>(set));
        }
    }

    public static <T> List<List<T>> partition(List<T> list, int batchSize) {
        return IntStream.range(0, list.size() / batchSize + 1)
                .mapToObj(i -> list.subList(i * batchSize,
                        Math.min(i * batchSize + batchSize, list.size())))
                .filter(s -> !s.isEmpty()).collect(Collectors.toList());
    }

    public static <T> List<T> add(List<T> list, T element) {
        final int size = list.size();
        if (size == 0) {
            return ImmutableList.of(element);
        } else if (list instanceof ImmutableList) {
            if (size == 1) {
                final T val = list.get(0);
                list = new ArrayList<>();
                list.add(val);
            } else {
                list = new ArrayList<>(list);
            }
        }
        list.add(element);
        return list;
    }

    public static <T> List<T> copyOf(List<T> list) {
        if (list instanceof ImmutableList) {
            return list;
        } else {
            return new ArrayList<>(list);
        }
    }

    public static <T> Set<T> add(Set<T> set, T element) {
        final int size = set.size();
        if (size == 0) {
            return ImmutableSet.of(element);
        } else if (set instanceof ImmutableSet) {
            if (size == 1) {
                final T val = set.iterator().next();
                set = new HashSet<>();
                set.add(val);
            } else {
                set = new HashSet<>(set);
            }
        }
        set.add(element);
        return set;
    }

    public static <T> Set<T> copyOf(Set<T> set) {
        if (set instanceof ImmutableSet) {
            return set;
        } else {
            return new HashSet<>(set);
        }
    }

    public static <T> Set<T> addSorted(Set<T> set, T element) {
        final int size = set.size();
        if (size == 0) {
            return ImmutableSet.of(element);
        } else if (set instanceof ImmutableSet) {
            if (size == 1) {
                final T val = set.iterator().next();
                set = new LinkedHashSet<>();
                set.add(val);
            } else {
                set = new LinkedHashSet<>(set);
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
            return new LinkedHashSet(set);
        }
    }

    public static <K,V> Map<K,V> put(Map<K,V> map, K key, V value) {
        final int size = map.size();
        if (size == 0) {
            return ImmutableMap.of(key, value);
        } else if (map instanceof ImmutableMap) {
            map = new HashMap<>(map);
        }
        map.put(key, value);
        return map;
    }

    public static <K,V> Map<K,V> copyOf(Map<K,V> map) {
        if (map instanceof ImmutableMap) {
            return map;
        } else {
            return new HashMap<>(map);
        }
    }

    private CollectionUtils() { }

}
