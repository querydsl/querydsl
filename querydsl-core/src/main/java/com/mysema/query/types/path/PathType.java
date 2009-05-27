package com.mysema.query.types.path;

import java.util.Collections;
import java.util.List;

import com.mysema.query.types.operation.Operator;

/**
 * The Class PathType.
 */
public enum PathType implements Operator<Path<?>> {
    ARRAYVALUE, 
    ARRAYVALUE_CONSTANT, 
    LISTVALUE, 
    LISTVALUE_CONSTANT, 
    MAPVALUE, 
    MAPVALUE_CONSTANT, 
    PROPERTY, 
    VARIABLE;

    @Override
    public List<Class<?>> getTypes() {
        return Collections.emptyList();
    }
}