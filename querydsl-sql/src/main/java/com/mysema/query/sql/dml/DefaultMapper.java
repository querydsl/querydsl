package com.mysema.query.sql.dml;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import com.mysema.query.QueryException;
import com.mysema.query.sql.RelationalPath;
import com.mysema.query.types.Path;

/**
 * @author tiwe
 *
 */
public class DefaultMapper implements Mapper<Object> {

    public static final DefaultMapper DEFAULT = new DefaultMapper();
    
    @Override
    public Map<Path<?>, Object> createMap(RelationalPath<?> entity, Object bean) {
        try {
            Map<Path<?>, Object> values = new HashMap<Path<?>, Object>();
            Class<?> beanClass = bean.getClass();
            Map<String, Field> fields = getPathFields(entity.getClass());
            for (Field beanField : beanClass.getDeclaredFields()) {
                if (!Modifier.isStatic(beanField.getModifiers())) {
                    Field field = fields.get(beanField.getName());                    
                    Path path = (Path<?>) field.get(entity);
                    beanField.setAccessible(true);
                    Object propertyValue = beanField.get(bean);
                    if (propertyValue != null) {
                        values.put(path, propertyValue);
                    }     
                }
            }
            return values;    
        } catch (IllegalAccessException e) {
            throw new QueryException(e);
        }
    }
    
    protected Map<String, Field> getPathFields(Class<?> cl) {
        Map<String, Field> fields = new HashMap<String, Field>();
        while (!cl.equals(Object.class)) {
            for (Field field : cl.getDeclaredFields()) {
                if (Path.class.isAssignableFrom(field.getType()) && !fields.containsKey(field.getName())) {
                    field.setAccessible(true);
                    fields.put(field.getName(), field);
                }
            }
            cl = cl.getSuperclass();
        }
        return fields;
    }

}
