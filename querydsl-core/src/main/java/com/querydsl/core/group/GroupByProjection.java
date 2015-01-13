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
package com.querydsl.core.group;

import java.util.LinkedHashMap;
import java.util.Map;

import com.querydsl.core.types.Expression;

/**
 * GroupByProjection provides projection of the Group results via the transform template method
 *
 * @author tiwe
 *
 * @param <K>
 * @param <V>
 */
public abstract class GroupByProjection<K,V> extends GroupByMap<K,V> {

    public GroupByProjection(Expression<K> key, Expression<?>... expressions) {
        super(key, expressions);
    }

    @Override
    protected Map<K, V> transform(Map<K, Group> groups) {
        Map<K, V> results = new LinkedHashMap<K, V>((int) Math.ceil(groups.size()/0.75), 0.75f);
        for (Map.Entry<K, Group> entry : groups.entrySet()) {
            results.put(entry.getKey(), transform(entry.getValue()));
        }
        return results;
    }

    /**
     * Creates a result object from the given group
     *
     * @param group
     * @return
     */
    protected abstract V transform(Group group);
}
