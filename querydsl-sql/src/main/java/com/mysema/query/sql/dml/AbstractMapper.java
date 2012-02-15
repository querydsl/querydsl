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

import com.mysema.query.sql.RelationalPath;
import com.mysema.query.types.Path;
import com.mysema.util.ReflectionUtils;

/**
 * Abstract base class for Mapper implementations
 * 
 * @author tiwe
 *
 * @param <T>
 */
public abstract class AbstractMapper<T> implements Mapper<T> {
    
    protected Map<String, Field> getPathFields(Class<?> cl) {
        Map<String, Field> fields = new HashMap<String, Field>();
        for (Field field : ReflectionUtils.getFields(cl)) {
            if (Path.class.isAssignableFrom(field.getType()) && !fields.containsKey(field.getName())) {
                field.setAccessible(true);
                fields.put(field.getName(), field);
            }
        }
        return fields;
    }
    
    protected boolean isPrimaryKeyColumn(RelationalPath<?> parent, Path<?> property) {
        return parent.getPrimaryKey() != null 
            && parent.getPrimaryKey().getLocalColumns().contains(property);
    }

}
