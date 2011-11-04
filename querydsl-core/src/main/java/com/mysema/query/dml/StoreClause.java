/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.dml;

import javax.annotation.Nullable;

import com.mysema.query.types.Expression;
import com.mysema.query.types.Path;

/**
 * Parent interface for InsertClause and UpdateCluase
 * 
 * @author tiwe
 *
 * @param <C> concrete subtype
 */
public interface StoreClause<C extends StoreClause<C>> extends DMLClause<C> {
    
    /**
     * Add a value binding
     *
     * @param <T>
     * @param path path to be updated
     * @param value value to set
     * @return
     */
    <T> C set(Path<T> path, @Nullable T value);
    
    /**
     * Add an expression binding
     * 
     * @param <T>
     * @param path
     * @param expression
     * @return
     */
    <T> C set(Path<T> path, Expression<? extends T> expression);
    
    /**
     * Bind the given path to null
     * 
     * @param path
     * @return
     */
    <T> C setNull(Path<T> path);


}
