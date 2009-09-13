/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.dml;

import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.path.Path;

/**
 * UpdateClause defines a generic extensible interface for Update clauses
 * 
 * @author tiwe
 *
 * @param <SubType>
 */
public interface UpdateClause<SubType extends UpdateClause<SubType>> {
    
    /**
     * Defines the filter constraints
     * 
     * @param o
     * @return
     */
    SubType where(EBoolean... o);
    
    /**
     * Set the paths to be updated
     * 
     * @param <T>
     * @param path path to be updated
     * @param value value to set
     * @return
     */
    <T> SubType set(Path<T> path, T value);
     
    /**
     * Execute the delete clause and return the amount of updated rows/items
     * 
     * @return
     */
    long execute();

}
