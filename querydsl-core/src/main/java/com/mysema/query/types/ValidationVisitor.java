/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types;

import java.util.List;

import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.operation.Operation;
import com.mysema.query.types.operation.Operator;

/**
 * ValidationVisitor provides validation functionality in visitor form
 * 
 * @author tiwe
 * @version $Id$
 */
public class ValidationVisitor extends EmptyVisitor<ValidationVisitor>{

    @Override
    protected void visit(Operation<?, ?> expr) {
        Operator<?> op = expr.getOperator();
        List<Expr<?>> args = expr.getArgs();
        if (op.getTypes().size() != args.size()) {
            throw new IllegalArgumentException(expr + " is invalid : "
                    + "args size mismatch : " + op.getTypes().size()
                    + " required, but " + args.size() + " given");
        }

    }

}
