/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types;

import com.mysema.query.types.custom.Custom;
import com.mysema.query.types.expr.EConstant;
import com.mysema.query.types.operation.Operation;
import com.mysema.query.types.path.Path;

/**
 * EmptyVisitor is an empty implementation of the Visitor class
 * 
 * @author tiwe
 *
 */
public class EmptyVisitor<SubType extends EmptyVisitor<SubType>> extends AbstractVisitor<SubType>{


    @Override
    protected void visit(Custom<?> expr) {
        
    }

    @Override
    protected void visit(EConstant<?> expr) {
        
    }

    @Override
    protected void visit(Operation<?, ?> expr) {
        
    }

    @Override
    protected void visit(Path<?> expr) {
        
    }

}
