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
package com.mysema.query.sql.dml;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.mysema.query.QueryException;
import com.mysema.query.sql.Column;
import com.mysema.query.sql.RelationalPath;
import com.mysema.query.types.Path;

/**
 * Creates the mapping via @Column annotated fields in the object. Field names don't have to match those in the RelationalPath.
 * 
 * @author tiwe
 *
 */
public class AnnotationMapper implements Mapper<Object> {

    public static final AnnotationMapper DEFAULT = new AnnotationMapper();
    
    @Override
    public Map<Path<?>, Object> createMap(RelationalPath<?> path, Object object) {
        try {
            Map<String, Path<?>> columnToPath = new HashMap<String, Path<?>>();
            for (Path<?> column : path.getColumns()) {
                columnToPath.put(column.getMetadata().getExpression().toString(), column);
            }
            Map<Path<?>, Object> values = new HashMap<Path<?>, Object>();        
            for (Field field : object.getClass().getDeclaredFields()) {
                Column ann = field.getAnnotation(Column.class);
                if (ann != null) {
                    field.setAccessible(true);
                    Object propertyValue = field.get(object);
                    if (propertyValue != null && columnToPath.containsKey(ann.value())) {
                        values.put(columnToPath.get(ann.value()), propertyValue);
                    }
                }
            }
            return values;    
        } catch (IllegalAccessException e) {
            throw new QueryException(e);
        }
        
    }

}
