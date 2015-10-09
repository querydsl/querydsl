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
package com.querydsl.sql.dml;

import java.util.Map;

import com.google.common.collect.Maps;
import com.querydsl.core.types.Path;
import com.querydsl.core.util.BeanMap;
import com.querydsl.sql.RelationalPath;
import com.querydsl.sql.types.Null;

/**
 * Creates the mapping by inspecting object via bean inspection.
 * Given bean doesn't need to have @Column metadata, but the fields need to have the same
 * name as in the given relational path.
 *
 * @author tiwe
 *
 */
public class BeanMapper extends AbstractMapper<Object> {

    public static final BeanMapper DEFAULT = new BeanMapper(false);

    public static final BeanMapper WITH_NULL_BINDINGS = new BeanMapper(true);

    private final boolean withNullBindings;

    public BeanMapper() {
        this(false);
    }

    public BeanMapper(boolean withNullBindings) {
        this.withNullBindings = withNullBindings;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public Map<Path<?>, Object> createMap(RelationalPath<?> entity, Object bean) {
        Map<Path<?>, Object> values = Maps.newLinkedHashMap();
        Map<String, Object> map = new BeanMap(bean);
        Map<String, Path<?>> columns = getColumns(entity);
        // populate in column order
        for (Map.Entry<String, Path<?>> entry : columns.entrySet()) {
            Path<?> path = entry.getValue();
            if (map.containsKey(entry.getKey())) {
                Object value = map.get(entry.getKey());
                if (value != null) {
                    values.put(path, value);
                } else if (withNullBindings && !isPrimaryKeyColumn(entity, path)) {
                    values.put(path, Null.DEFAULT);
                }
            }
        }
        return values;
    }

}
