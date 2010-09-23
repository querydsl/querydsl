/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.dml;

import com.mysema.query.types.Path;
import com.mysema.query.types.SubQueryExpression;

/**
 * InsertClause defines a generic interface for Insert clauses
 *
 * @author tiwe
 *
 * @param <C>
 */
public interface InsertClause<C extends InsertClause<C>> extends StoreClause<C>{

    /**
     * Define the columns to be populated
     *
     * @param columns
     * @return
     */
    C columns(Path<?>... columns);

    /**
     * Define the populate via subquery
     *
     * @param subQuery
     * @return
     */
    C select(SubQueryExpression<?> subQuery);

    /**
     * Define the value bindings
     *
     * @param v
     * @return
     */
    C values(Object... v);

}
