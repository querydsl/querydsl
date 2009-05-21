/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types;

import java.util.List;

import com.mysema.query.types.alias.ASimple;
import com.mysema.query.types.alias.AToPath;
import com.mysema.query.types.custom.Custom;
import com.mysema.query.types.expr.EConstant;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.operation.Operation;
import com.mysema.query.types.operation.Ops.Op;
import com.mysema.query.types.path.Path;

/**
 * ValidationVisitor provides
 *
 * @author tiwe
 * @version $Id$
 */
public class ValidationVisitor extends AbstractVisitor<ValidationVisitor>{

    @Override
    protected void visit(ASimple<?> expr) {
        // TODO Auto-generated method stub        
    }

    @Override
    protected void visit(AToPath expr) {
        // TODO Auto-generated method stub        
    }

    @Override
    protected void visit(Custom<?> expr) {
        // TODO Auto-generated method stub        
    }

    @Override
    protected void visit(EConstant<?> expr) {
        // TODO Auto-generated method stub        
    }

    @Override
    protected void visit(Operation<?, ?> expr) {
    	Op<?> op = expr.getOperator();
        List<Expr<?>> args = expr.getArgs();
        if (op.getTypes().size() != args.size()){
            throw new IllegalArgumentException(expr + " is invalid : " +
                "args size mismatch : " + 
                op.getTypes().size() + " required, but " + args.size() + " given");
        }
        
    }

    @Override
    protected void visit(Path<?> expr) {
        // TODO Auto-generated method stub        
    }

}
