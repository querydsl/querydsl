/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql;

import com.mysema.query.SimpleProjectable;
import com.mysema.query.types.OrderSpecifier;

/**
 * Union defines an interface for Union queries
 *
 * @author tiwe
 *
 * @param <RT>
 */
public interface Union<RT> extends SimpleProjectable<RT>{

    /**
     * Define the ordering of the query results
     *
     * @param o
     * @return
     */
    Union<RT> orderBy(OrderSpecifier<?>... o);

}
