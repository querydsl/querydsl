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
 * @param <Q>
 */
public interface DeleteClause<Q extends DeleteClause<Q>> {

    /**
     * Defines the filter constraints
     * 
     * @param o
     * @return
     */
    Q where(EBoolean... o);
    
    /**
     * Execute the delete clause and return the amount of deleted rows/items
     * 
     * @return
     */
    long execute();
}
