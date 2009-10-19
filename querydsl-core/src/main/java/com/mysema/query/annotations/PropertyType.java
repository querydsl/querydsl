/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.annotations;

/**
 * PropertyType defines the Path type to be used for a Domain property
 * 
 * @author tiwe
 *
 */
public enum PropertyType {
    /**
     * for PComparable fields
     */
    COMPARABLE,
    /**
     *  for PDate fields
     */
    DATE,
    /**
     *  for PDateTime fields
     */
    DATETIME,
    /**
     *  to skip properties
     */
    NONE,
    /**
     * for PNumber fields
     */
    NUMERIC,
    /**
     * for PSimple fields
     */
    SIMPLE,
    /**
     * for PTime fields
     */
    TIME  

}
