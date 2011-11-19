package com.mysema.query.sql.dml;

import java.util.Map;

import com.mysema.query.sql.RelationalPath;
import com.mysema.query.types.Path;

/**
 * Create a Map of updates for a given domain object
 * 
 * @author tiwe
 *
 */
public interface Mapper<T> {
    
    /**
     * @param path
     * @param object
     * @return
     */
    Map<Path<?>, Object> createMap(RelationalPath<?> path, T object);

}
