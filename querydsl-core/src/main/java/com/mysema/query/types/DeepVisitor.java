/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types;

import com.mysema.query.types.alias.ASimple;
import com.mysema.query.types.alias.AToPath;
import com.mysema.query.types.custom.Custom;
import com.mysema.query.types.expr.EConstant;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.operation.Operation;
import com.mysema.query.types.path.Path;
import com.mysema.query.types.quant.Quant;

/**
 * DeepVisitor provides
 * 
 * @author tiwe
 * @version $Id$
 */
public class DeepVisitor<SubType extends DeepVisitor<SubType>> extends
        AbstractVisitor<SubType> {

    @Override
    protected void visit(ASimple<?> expr) {

    }

    @Override
    protected void visit(AToPath expr) {

    }

    @Override
    protected void visit(Custom<?> expr) {

    }

    @Override
    protected void visit(EConstant<?> expr) {

    }

    @Override
    protected void visit(Operation<?, ?> o) {
        for (Expr<?> expr : o.getArgs())
            handle(expr);
    }

    @Override
    protected void visit(Path<?> expr) {
        // handle parent paths ?!?
    }

    @Override
    protected void visit(Quant<?> q) {
        // TODO Auto-generated method stub

    }

}
