/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.dml;

import com.mysema.query.types.expr.EBoolean;

/**
 * DeleteClause defines a generic extensible interface for Delete clauses
 * 
 * @author tiwe
 *
 * @param <SubType>
 */
public interface DeleteClause<SubType extends DeleteClause<SubType>> {

    /**
     * Defines the filter constraints
     * 
     * @param o
     * @return
     */
    SubType where(EBoolean... o);
    
    /**
     * Execute the delete clause and return the amount of deleted rows/items
     * 
     * @return
     */
    long execute();
}
