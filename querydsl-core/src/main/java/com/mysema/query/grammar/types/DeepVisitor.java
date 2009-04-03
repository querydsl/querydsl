/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar.types;

import com.mysema.query.grammar.types.Alias.ASimple;
import com.mysema.query.grammar.types.Alias.AToPath;
import com.mysema.query.grammar.types.Expr.EConstant;

/**
 * VisitorBase provides
 *
 * @author tiwe
 * @version $Id$
 */
public class DeepVisitor<SubType extends DeepVisitor<SubType>> extends AbstractVisitor<SubType> {

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
        for (Expr<?> expr : o.getArgs()) handle(expr);
    }

    @Override
    protected void visit(Path<?> expr) {
        // handle parent paths ?!?        
    }

}
