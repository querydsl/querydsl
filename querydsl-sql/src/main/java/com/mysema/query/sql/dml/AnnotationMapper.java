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
