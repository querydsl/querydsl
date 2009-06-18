/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.query.ListSubQuery;
import com.mysema.query.types.query.ObjectSubQuery;

/**
 * @author tiwe
 *
 */
public interface Detachable {

    /**
     * @return
     */
    ObjectSubQuery<Long> countExpr();
    
    /**
     * Create a projection expression for the given projection
     * 
     * @param first
     * @param second
     * @param rest
     *            rest
     * @return a List over the projection
     */
    ListSubQuery<Object[]> listExpr(Expr<?> first, Expr<?> second, Expr<?>... rest);

    /**
     * Create a projection expression for the given projection
     * 
     * @param <RT>
     *            generic type of the List
     * @param projection
     * @return a List over the projection
     */
    <RT> ListSubQuery<RT> listExpr(Expr<RT> projection);
    
    /**
     * Create a projection expression for the given projection
     * 
     * @param first
     * @param second
     * @param rest
     * @return
     */
    ObjectSubQuery<Object[]> uniqueExpr(Expr<?> first, Expr<?> second, Expr<?>... rest);

    /**
     * Create a projection expression for the given projection
     * 
     * @param <RT>
     *            return type
     * @param projection
     * @return the result or null for an empty result
     */
    <RT> ObjectSubQuery<RT> uniqueExpr(Expr<RT> projection);
       

}
