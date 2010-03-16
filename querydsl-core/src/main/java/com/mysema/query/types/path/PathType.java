/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.path;

import java.util.Collections;
import java.util.List;

import com.mysema.query.types.operation.Operator;

/**
 * PathType represents the relation of a path to its parent
 */
public enum PathType implements Operator<Path<?>> {
    /**
     * Indexed array access (array[i])
     */
    ARRAYVALUE(false), 
    /**
     * Indexed array access with constant (array[i])
     */
    ARRAYVALUE_CONSTANT(false), 
    /**
     * Indexed list access (list.get(index))
     */
    LISTVALUE(true), 
    /**
     * Indexed list access with constant (list.get(index))
     */
    LISTVALUE_CONSTANT(true), 
    /**
     * Map value access (map.get(key))
     */
    MAPVALUE(true), 
    /**
     * Map value access with constant (map.get(key))
     */
    MAPVALUE_CONSTANT(true), 
    /**
     * Property of the parent
     */
    PROPERTY(false), 
    /**
     * Root path
     */
    VARIABLE(false);
    
    private final boolean generic;
    
    PathType(boolean generic){
        this.generic = generic;
    }

    @Override
    public List<Class<?>> getTypes() {
        return Collections.emptyList();
    }
    
    public boolean isGeneric(){
        return generic;
    }
}