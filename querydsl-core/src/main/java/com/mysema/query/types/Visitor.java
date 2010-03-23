/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types;



/**
 * Visitor provides a dispatching Visitor for Expr instances.
 * 
 * @author tiwe
 * @version $Id$
 */
public interface Visitor {

    /**
     * @param expr
     */
    void visit(Constant<?> expr);

    /**
     * @param expr
     */
    void visit(Custom<?> expr);
    
    /**
     * @param expr
     */
    void visit(EArrayConstructor<?> expr);

    /**
     * @param expr
     */
    void visit(EConstructor<?> expr);
    
    /**
     * @param expr
     */
    void visit(Operation<?, ?> expr);

    /**
     * @param expr
     */
    void visit(Path<?> expr);
    
    /**
     * @param query
     */
    void visit(SubQuery<?> query);
    
}
