package com.mysema.query.dml;

import com.mysema.query.types.expr.EBoolean;

/**
 * Parent interface for DeleteClause and UpdateCluase
 * 
 * @author tiwe
 *
 * @param <C>
 */
public interface FilteredClause<C extends FilteredClause<C>> {
    
    /**
     * Execute the clause and return the amount of deleted/inserted/updated rows/items
     *
     * @return
     */
    long execute();
    
    /**
     * Defines the filter constraints
     * 
     * @param o
     * @return
     */
    C where(EBoolean... o);

}
