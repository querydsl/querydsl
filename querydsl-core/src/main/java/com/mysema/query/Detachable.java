/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import com.mysema.query.types.ListSubQuery;
import com.mysema.query.types.ObjectSubQuery;
import com.mysema.query.types.expr.Expr;

/**
 * @author tiwe
 *
 */
public interface Detachable<JM> {

    /**
     * @return
     */
    ObjectSubQuery<JM,Long> countExpr();
    
    /**
     * Create a projection expression for the given projection
     * 
     * @param first
     * @param second
     * @param rest
     *            rest
     * @return a List over the projection
     */
    ListSubQuery<JM,Object[]> listExpr(Expr<?> first, Expr<?> second, Expr<?>... rest);

    /**
     * Create a projection expression for the given projection
     * 
     * @param <RT>
     *            generic type of the List
     * @param projection
     * @return a List over the projection
     */
    <RT> ListSubQuery<JM,RT> listExpr(Expr<RT> projection);
    
    /**
     * Create a projection expression for the given projection
     * 
     * @param first
     * @param second
     * @param rest
     * @return
     */
    ObjectSubQuery<JM,Object[]> uniqueExpr(Expr<?> first, Expr<?> second, Expr<?>... rest);

    /**
     * Create a projection expression for the given projection
     * 
     * @param <RT>
     *            return type
     * @param projection
     * @return the result or null for an empty result
     */
    <RT> ObjectSubQuery<JM,RT> uniqueExpr(Expr<RT> projection);
       

}
