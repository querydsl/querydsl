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

import java.util.LinkedHashMap;
import java.util.Map;

import com.mysema.commons.lang.Pair;

/**
 * @author tiwe
 *
 * @param <K>
 * @param <V>
 */
class GMap<K, V> extends AbstractGroupExpression<Pair<K, V>, Map<K, V>> {
    
    private static final long serialVersionUID = 7106389414200843920L;

    public GMap(QPair<K,V> qpair) {
        super(Map.class, qpair);
    }

    @Override
    public GroupCollector<Pair<K,V>, Map<K, V>> createGroupCollector() {
        return new GroupCollector<Pair<K,V>, Map<K, V>>() {

            private final Map<K, V> map = new LinkedHashMap<K, V>();
            
            @Override
            public void add(Pair<K,V> pair) {
                map.put(pair.getFirst(), pair.getSecond());
            }

            @Override
            public Map<K, V> get() {
                return map;
            }
            
        };
    }
}