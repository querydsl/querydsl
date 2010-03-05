/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types;

import com.mysema.query.types.custom.Custom;
import com.mysema.query.types.expr.Constant;
import com.mysema.query.types.expr.EArrayConstructor;
import com.mysema.query.types.expr.EConstructor;
import com.mysema.query.types.operation.Operation;
import com.mysema.query.types.path.Path;
import com.mysema.query.types.query.SubQuery;

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
    void visit(SubQuery query);
    
}
