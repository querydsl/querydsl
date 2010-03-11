/*
 * Copyright (c) 2009 Mysema Ltd.
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
    public C columns(Path<?>... columns);

    /**
     * Execute the insert clause and return the amount of inserted rows/items
     * 
     * @return
     */
    public long execute();

    /**
     * Define the populate via subquery
     * 
     * @param subQuery
     * @return
     */
    public C select(SubQuery<?> subQuery);

    /**
     * Define the value bindings
     * 
     * @param v
     * @return
     */
    public C values(Object... v);

}