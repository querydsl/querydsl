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
package com.mysema.query.util;

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
        if (list.isEmpty()) {
            return ImmutableList.of(element);
        } else if (list.size() == 1) {
            final List<T> old = list;
            list = Lists.newArrayList();
            list.add(old.get(0));
        }
        list.add(element);
        return list;
    }
    
    public static <T> Set<T> add(Set<T> set, T element) {
        if (set.isEmpty()) {
            return ImmutableSet.of(element);
        } else if (set.size() == 1) {
            final Set<T> old = set;
            set = Sets.newHashSet();
            set.add(old.iterator().next());
        }
        set.add(element);
        return set;
    }
    
    public static <T> Set<T> addSorted(Set<T> set, T element) {
        if (set.isEmpty()) {
            return ImmutableSet.of(element);
        } else if (set.size() == 1) {
            Set<T> old = set;
            set = Sets.newLinkedHashSet();
            set.add(old.iterator().next());
        }
        set.add(element);
        return set;
    }
    
    public static <K,V> Map<K,V> put(Map<K,V> map, K key, V value) {
        if (map.isEmpty()) {
            return ImmutableMap.of(key, value);
        } else if (map.size() == 1) {
            map = Maps.newHashMap(map);
        }
        map.put(key, value);
        return map;
    }
    
    private CollectionUtils() {}
    
}
