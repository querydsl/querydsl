package com.mysema.query.sql.dml;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

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

}
