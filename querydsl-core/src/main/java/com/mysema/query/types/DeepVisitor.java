/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types;

import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.operation.Operation;

/**
 * DeepVisitor provides deep dispatching functionality
 * 
 * @author tiwe
 * @version $Id$
 */
public abstract class DeepVisitor<SubType extends DeepVisitor<SubType>> extends EmptyVisitor<SubType> {

    @Override
    protected void visit(Operation<?, ?> o) {
        for (Expr<?> expr : o.getArgs()){
            handle(expr);
        }            
    }

}
