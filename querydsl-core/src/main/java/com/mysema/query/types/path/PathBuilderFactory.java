/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.path;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * @author tiwe
 *
 */
public class PathBuilderFactory {

    private final Map<Class<?>, PathBuilder<?>> paths = new HashMap<Class<?>, PathBuilder<?>>();
    
    @SuppressWarnings("unchecked")
    public <T> PathBuilder<T> create(Class<T> clazz){
        PathBuilder<T> rv = (PathBuilder<T>) paths.get(clazz);
        if (rv == null){
            rv = new PathBuilder<T>(clazz, StringUtils.uncapitalize(clazz.getSimpleName()));
            paths.put(clazz, rv);
        }
        return rv;
    }
    
}
