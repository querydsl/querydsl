/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.dml;

import com.mysema.query.types.expr.EBoolean;

/**
 * DeleteClause defines a generic extensible interface for Delete clauses
 *
 * @author tiwe
 *
 * @param <C>
 */
public interface DeleteClause<C extends DeleteClause<C>> extends DMLClause<C>{

    /**
     * Defines the filter constraints
     *
     * @param o
     * @return
     */
    C where(EBoolean... o);

}
