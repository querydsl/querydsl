/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types;

import java.io.Serializable;
import java.util.List;

/**
 * Operator represents operator symbols
 *
 * @author tiwe
 *
 * @param <RT>
 */
public interface Operator<RT> extends Serializable{
    
    /**
     * Get the unique id for this Operator
     * 
     * @return
     */
    String getId();
    
    /**
     * Get the types related to this operator symbols
     *
     * @return
     */
    List<Class<?>> getTypes();

}
