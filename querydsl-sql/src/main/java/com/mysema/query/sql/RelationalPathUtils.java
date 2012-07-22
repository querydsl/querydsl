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
package com.mysema.query.sql;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import com.mysema.query.QueryException;
import com.mysema.query.types.Expression;
import com.mysema.query.types.FactoryExpression;
import com.mysema.query.types.Path;
import com.mysema.query.types.Projections;
import com.mysema.query.types.QBean;
import com.mysema.util.ReflectionUtils;

/**
 * RelationalPathUtils provides static utility methods for {@link RelationalPath} instances
 * 
 * @author tiwe
 *
 */
@SuppressWarnings("unchecked")
public final class RelationalPathUtils {
    
    public static <T> FactoryExpression<T> createProjection(RelationalPath<T> path) {
        if (path.getType().equals(path.getClass())) {
            throw new IllegalArgumentException("RelationalPath based projection can only be used with generated Bean types");
        }                       
        try {
            // ensure that empty constructor is available
            path.getType().getConstructor();
            return createBeanProjection(path);
        } catch (NoSuchMethodException e) {
            // fallback to constructor projection
            return createConstructorProjection(path);
        }        
                    
    }
    
    private static <T> FactoryExpression<T> createConstructorProjection(RelationalPath<T> path) {
        Expression<?>[] exprs = path.getColumns().toArray(new Expression[path.getColumns().size()]);
        return Projections.<T>constructor((Class)path.getType(), exprs);
    }
    
    private static <T> FactoryExpression<T> createBeanProjection(RelationalPath<T> path) {
        try {
            Map<String,Expression<?>> bindings = new HashMap<String,Expression<?>>();
            for (Field field : ReflectionUtils.getFields(path.getClass())) {
                if (Path.class.isAssignableFrom(field.getType()) 
                        && !Modifier.isStatic(field.getModifiers())) {
                    field.setAccessible(true);
                    Path<?> column = (Path<?>) field.get(path);
                    if (path.equals(column.getMetadata().getParent())) {
                        bindings.put(field.getName(), column);
                    }                    
                }
            }            
            if (bindings.isEmpty()) {
                throw new IllegalArgumentException("No bindings could be derived from " + path);
            }                
            return new QBean<T>((Class)path.getType(), true, bindings);
        } catch(IllegalAccessException e) {
            throw new QueryException(e);
        }
    }
    
    private RelationalPathUtils() {}

}
