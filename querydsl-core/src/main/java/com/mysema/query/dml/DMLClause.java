/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.dml;

/**
 * Parent interface for DML clauses
 * 
 * @author tiwe
 *
 * @param <C> concrete subtype
 */
public interface DMLClause<C extends DMLClause<C>> {

    /**
     * Execute the clause and return the amount of affected rows
     *
     * @return
     */
    long execute();
}
