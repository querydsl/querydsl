/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql;

import java.util.List;

import com.mysema.commons.lang.CloseableIterator;
import com.mysema.query.types.OrderSpecifier;

/**
 * Union defines an interface for Union queries
 *
 * @author tiwe
 *
 * @param <RT> return type of projection
 */
public interface Union<RT> {
    
    /**
     * Get the projection as a typed List
     *
     * @return
     */
    List<RT> list();
    
    /**
     * Get the projection as a typed Iterator
     *
     * @return
     */
    CloseableIterator<RT> iterate();

    /**
     * Define the ordering of the query results
     *
     * @param o
     * @return
     */
    Union<RT> orderBy(OrderSpecifier<?>... o);

}
