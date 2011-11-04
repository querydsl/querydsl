/*
 * Copyright (c) 2011 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.group;

import java.util.LinkedHashMap;
import java.util.Map;

import com.mysema.commons.lang.Pair;

class GMap<K, V> extends AbstractGroupExpression<Pair<K, V>, Map<K, V>> {
    
    private static final long serialVersionUID = 7106389414200843920L;

    public GMap(QPair<K,V> qpair) {
        super(Map.class, qpair);
    }

    @Override
    public GroupCollector<Pair<K,V>, Map<K, V>> createGroupCollector() {
        return new GroupCollector<Pair<K,V>, Map<K, V>>() {

            private final Map<K, V> set = new LinkedHashMap<K, V>();
            
            @Override
            public void add(Pair<K,V> pair) {
                set.put(pair.getFirst(), pair.getSecond());
            }

            @Override
            public Map<K, V> get() {
                return set;
            }
            
        };
    }
}