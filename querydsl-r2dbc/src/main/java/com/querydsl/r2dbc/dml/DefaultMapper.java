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
package com.querydsl.r2dbc.dml;

import com.google.common.collect.Maps;
import com.querydsl.core.QueryException;
import com.querydsl.core.types.Path;
import com.querydsl.core.util.ReflectionUtils;
import com.querydsl.r2dbc.types.Null;
import com.querydsl.sql.RelationalPath;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;

/**
 * Creates the mapping by inspecting the RelationalPath and Object via reflection.
 * Given bean doesn't need to have @Column metadata, but the fields need to have the same
 * name as in the given relational path.
 *
 * @author mc_fish
 */
public class DefaultMapper extends AbstractMapper<Object> {

    public static final DefaultMapper DEFAULT = new DefaultMapper(false);

    public static final DefaultMapper WITH_NULL_BINDINGS = new DefaultMapper(true);

    private final boolean withNullBindings;

    public DefaultMapper() {
        this(false);
    }

    public DefaultMapper(boolean withNullBindings) {
        this.withNullBindings = withNullBindings;
    }

    @Override
    public Map<Path<?>, Object> createMap(RelationalPath<?> entity, Object bean) {
        try {
            Map<Path<?>, Object> values = Maps.newLinkedHashMap();
            Class<?> beanClass = bean.getClass();
            Map<String, Path<?>> columns = getColumns(entity);
            // populate in column order
            for (Map.Entry<String, Path<?>> entry : columns.entrySet()) {
                Path<?> path = entry.getValue();
                Field beanField = ReflectionUtils.getFieldOrNull(beanClass, entry.getKey());
                if (beanField != null && !Modifier.isStatic(beanField.getModifiers())) {
                    beanField.setAccessible(true);
                    Object propertyValue = beanField.get(bean);
                    if (propertyValue != null) {
                        values.put(path, propertyValue);
                    } else if (withNullBindings && !isPrimaryKeyColumn(entity, path)) {
                        values.put(path, Null.DEFAULT);
                    }
                }
            }
            return values;
        } catch (IllegalAccessException e) {
            throw new QueryException(e);
        }
    }


}
