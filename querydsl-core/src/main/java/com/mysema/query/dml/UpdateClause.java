/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.dml;

import java.util.List;

import com.mysema.query.types.Path;
import com.mysema.query.types.expr.EBoolean;

/**
 * UpdateClause defines a generic extensible interface for Update clauses
 *
 * @author tiwe
 *
 * @param <C>
 */
public interface UpdateClause<C extends UpdateClause<C>> extends StoreClause<C>, FilteredClause<C>{

    /**
     * Set the paths to be updated
     *
     * @param paths
     * @param values
     * @return
     */
    C set(List<? extends Path<?>> paths, List<?> values);

    /**
     * Defines the filter constraints
     *
     * @param o
     * @return
     */
    C where(EBoolean... o);

}
