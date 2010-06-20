/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.dml;

import javax.annotation.Nullable;

import com.mysema.query.types.Path;
import com.mysema.query.types.SubQuery;

/**
 * InsertClause defines a generic extensible interface for Insert clauses
 * 
 * @author tiwe
 *
 * @param <C>
 */
public interface InsertClause<C extends InsertClause<C>> {

    /**
     * Define the columns to be populated
     * 
     * @param columns
     * @return
     */
    C columns(Path<?>... columns);
    
    /**
     * Execute the insert clause and return the amount of inserted rows/items
     * 
     * @return
     */
    long execute();

    /**
     * Define the populate via subquery
     * 
     * @param subQuery
     * @return
     */
    C select(SubQuery<?> subQuery);

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
     * Define the value bindings
     * 
     * @param v
     * @return
     */
    C values(Object... v);

}