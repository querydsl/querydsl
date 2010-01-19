/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

/**
 * @author tiwe
 *
 */
public interface SerializerConfig {
    
    boolean useEntityAccessors();
    
    boolean useListAccessors();
    
    boolean useMapAccessors();
    
    boolean createDefaultVariable();

}
