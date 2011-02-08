/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query;

import com.mysema.query.types.Predicate;

/**
 * Parent interface for clauses with a filter condition
 *
 * @author tiwe
 *
 * @param <C>
 */
public interface FilteredClause<C extends FilteredClause<C>> {

    /**
     * Adds the given filter conditions
     *
     * @param o filter conditions to be added
     * @return
     */
    C where(Predicate... o);

}
