/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.query.ListSubQuery;
import com.mysema.query.types.query.ObjectSubQuery;

/**
 * Detachable defines methods for the construction of SubQuery instances
 * 
 * @author tiwe
 *
 */
public interface Detachable {

    /**
     * Return the count of matched rows as a subquery
     * 
     * @return
     */
    ObjectSubQuery<Long> count();
    
    /**
     * Create a projection expression for the given projection
     * 
     * @param first
     * @param second
     * @param rest
     *            rest
     * @return a List over the projection
     */
    ListSubQuery<Object[]> list(Expr<?> first, Expr<?> second, Expr<?>... rest);

    /**
     * Create a projection expression for the given projection
     * 
     * @param <RT>
     *            generic type of the List
     * @param projection
     * @return a List over the projection
     */
    <RT> ListSubQuery<RT> list(Expr<RT> projection);
    
    /**
     * Create a projection expression for the given projection
     * 
     * @param first
     * @param second
     * @param rest
     * @return
     */
    ObjectSubQuery<Object[]> unique(Expr<?> first, Expr<?> second, Expr<?>... rest);

    /**
     * Create a projection expression for the given projection
     * 
     * @param <RT>
     *            return type
     * @param projection
     * @return the result or null for an empty result
     */
    <RT> ObjectSubQuery<RT> unique(Expr<RT> projection);
    
    /**
     * @return
     */
    EBoolean exists();

    /**
     * @return
     */
    EBoolean notExists();  

}
