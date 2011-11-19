package com.mysema.query.sql.dml;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections15.BeanMap;

import com.mysema.query.QueryException;
import com.mysema.query.sql.RelationalPath;
import com.mysema.query.types.Path;

/**
 * @author tiwe
 *
 */
public class BeanMapper implements Mapper<Object> {
    
    public static final BeanMapper DEFAULT = new BeanMapper();

    @SuppressWarnings("rawtypes")
    @Override
    public Map<Path<?>, Object> createMap(RelationalPath<?> entity, Object bean) {
        try {
            Map<Path<?>, Object> values = new HashMap<Path<?>, Object>();
            BeanMap map = new BeanMap(bean);
            for (Map.Entry entry : map.entrySet()) {
                String property = entry.getKey().toString();
                if (!property.equals("class")) {
                    Field field = entity.getClass().getDeclaredField(property);
                    field.setAccessible(true);
                    Path path = (Path<?>) field.get(entity);
                    if (entry.getValue() != null) {
                        values.put(path, entry.getValue());    
                    }                    
                }
            }      
            return values;
        } catch (IllegalAccessException e) {
            throw new QueryException(e);
        } catch (SecurityException e) {
            throw new QueryException(e);
        } catch (NoSuchFieldException e) {
            throw new QueryException(e);
        }      
    }

}
