/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.dml;

import com.mysema.query.types.path.Path;
import com.mysema.query.types.query.SubQuery;

/**
 * InsertClause defines a generic extensible interface for Insert clauses
 * 
 * @author tiwe
 *
 * @param <C>
 */
public interface InsertClause<C extends InsertClause<C>> {

    /**
     * Define the columns of to be populated
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
     * Define the value bindings
     * 
     * @param v
     * @return
     */
    C values(Object... v);

}