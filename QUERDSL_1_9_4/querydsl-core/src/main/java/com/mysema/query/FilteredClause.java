/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query;

import com.mysema.query.types.expr.EBoolean;

/**
 * Parent interface for clauses with a filter
 * 
 * @author tiwe
 *
 * @param <C>
 */
public interface FilteredClause<C extends FilteredClause<C>> {

    /**
     * Defines the filter constraints
     * 
     * @param o
     * @return
     */
    C where(EBoolean... o);

}
