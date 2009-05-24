/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.alias;

import com.mysema.query.types.expr.Expr;

/**
 * AliasFactory is a factory interface for alias creation
 *
 * @author tiwe
 * @version $Id$
 */
interface AliasFactory {

    /**
     * 
     * @param <A>
     * @param cl
     * @param parent
     * @param path
     * @return
     */
    <A> A createAliasForProp(Class<A> cl, Object parent, Expr<?> path);

    /**
     * 
     * @param <A>
     * @param cl
     * @param var
     * @return
     */
    <A> A createAliasForVar(Class<A> cl, String var);
    
    /**
     * 
     * @param <A>
     * @param cl
     * @param expr
     * @return
     */
    <A> A createAliasForExpr(Class<A> cl, Expr<? extends A> expr);
    
    /**
     * 
     * @param <A>
     * @return
     */
    <A extends Expr<?>> A getCurrent();

    /**
     * 
     * @param <A>
     * @return
     */
    <A extends Expr<?>> A getCurrentAndReset();
    
    /**
     * 
     * @param path
     */
    void setCurrent(Expr<?> path);
    
    /**
     * 
     */
    void reset();

    

}