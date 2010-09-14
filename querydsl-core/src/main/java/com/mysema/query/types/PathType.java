/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types;

import java.util.Collections;
import java.util.List;

/**
 * PathType represents the relation of a {@link Path} to its parent
 */
public enum PathType implements Operator<Path<?>> {
    /**
     * Indexed array access (array[i])
     */
    ARRAYVALUE,
    /**
     * Indexed array access with constant (array[i])
     */
    ARRAYVALUE_CONSTANT,
    /**
     * Indexed list access (list.get(index))
     */
    LISTVALUE,
    /**
     * Indexed list access with constant (list.get(index))
     */
    LISTVALUE_CONSTANT,
    /**
     * Map value access (map.get(key))
     */
    MAPVALUE,
    /**
     * Map value access with constant (map.get(key))
     */
    MAPVALUE_CONSTANT,
    /**
     * Property of the parent
     */
    PROPERTY,
    /**
     * Root path
     */
    VARIABLE;

    @Override
    public List<Class<?>> getTypes() {
        return Collections.emptyList();
    }

    @Override
    public String getId() {
        return name();
    }

}
