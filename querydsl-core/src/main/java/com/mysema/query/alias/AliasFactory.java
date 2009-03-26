/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.alias;

import com.mysema.query.grammar.types.Expr;

/**
 * AliasFactory provides
 *
 * @author tiwe
 * @version $Id$
 */
public interface AliasFactory {

    <A> A createAliasForProp(Class<A> cl, Object parent, Expr<?> path);

    <A> A createAliasForVar(Class<A> cl, String var);
    
    <A> A createAliasForExpr(Class<A> cl, Expr<? extends A> expr);
    
    <A extends Expr<?>> A getCurrent();

    <A extends Expr<?>> A getCurrentAndReset();
    
    void setCurrent(Expr<?> path);
    
    void reset();

    

}