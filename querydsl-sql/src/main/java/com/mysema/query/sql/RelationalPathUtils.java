package com.mysema.query.sql;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import com.mysema.query.QueryException;
import com.mysema.query.types.Expression;
import com.mysema.query.types.FactoryExpression;
import com.mysema.query.types.Path;
import com.mysema.query.types.QBean;
import com.mysema.util.ReflectionUtils;

public final class RelationalPathUtils {
    
    public static <T> FactoryExpression<T> createProjection(RelationalPath<T> path) {
        if (path.getType().equals(path.getClass())) {
            throw new IllegalArgumentException("RelationalPath based projection can only be used with generated Bean types");
        }                       
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
