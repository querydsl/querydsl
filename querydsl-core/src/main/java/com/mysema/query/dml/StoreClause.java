/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.dml;

import javax.annotation.Nullable;

import com.mysema.query.types.Path;

/**
 * Parent interface for InsertClause and UpdateCluase
 * 
 * @author tiwe
 *
 * @param <C>
 */
public interface StoreClause<C extends StoreClause<C>> {
    
    /**
     * Execute the clause and return the amount of deleted/inserted/updated rows/items
     *
     * @return
     */
    long execute();
    
    /**
     * Add a value binding
     *
     * @param <T>
     * @param path path to be updated
     * @param value value to set
     * @return
     */
    <T> C set(Path<T> path, @Nullable T value);


}
