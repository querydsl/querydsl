/*
 * Copyright (c) 2011 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.alias;

/**
 * @author tiwe
 *
 */
public interface TypeSystem {
    
    boolean isCollectionType(Class<?> cl);
    
    boolean isSetType(Class<?> cl);
    
    boolean isListType(Class<?> cl);
    
    boolean isMapType(Class<?> cl);

}
