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
package com.querydsl.sql.dml;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.querydsl.core.QueryException;
import com.querydsl.sql.Column;
import com.querydsl.sql.ColumnMetadata;
import com.querydsl.sql.RelationalPath;
import com.querydsl.sql.types.Null;
import com.querydsl.core.types.Path;
import com.querydsl.core.util.ReflectionUtils;

/**
 * Creates the mapping via @Column annotated fields in the object. Field names don't have to match those in the RelationalPath.
 *
 * @author tiwe
 *
 */
public class AnnotationMapper implements Mapper<Object> {

    public static final AnnotationMapper DEFAULT = new AnnotationMapper(false);

    public static final AnnotationMapper WITH_NULL_BINDINGS = new AnnotationMapper(true);

    private final boolean withNullBindings;

    public AnnotationMapper() {
        this(false);
    }

    public AnnotationMapper(boolean withNullBindings) {
        this.withNullBindings = withNullBindings;
    }

    @Override
    public Map<Path<?>, Object> createMap(RelationalPath<?> path, Object object) {
        try {
            Map<String, Path<?>> columnToPath = new HashMap<String, Path<?>>();
            for (Path<?> column : path.getColumns()) {
                columnToPath.put(ColumnMetadata.getName(column), column);
            }
            Map<Path<?>, Object> values = new HashMap<Path<?>, Object>();
            for (Field field : ReflectionUtils.getFields(object.getClass())) {
                Column ann = field.getAnnotation(Column.class);
                if (ann != null) {
                    field.setAccessible(true);
                    Object propertyValue = field.get(object);
                    if (propertyValue != null) {
                        if (columnToPath.containsKey(ann.value())) {
                            values.put(columnToPath.get(ann.value()), propertyValue);
                        }
                    } else if (withNullBindings) {
                        values.put(columnToPath.get(ann.value()), Null.DEFAULT);
                    }
                }
            }
            return values;
        } catch (IllegalAccessException e) {
            throw new QueryException(e);
        }

    }

}
