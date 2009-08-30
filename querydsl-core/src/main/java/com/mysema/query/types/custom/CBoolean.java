/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.custom;

import com.mysema.query.types.Visitor;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.Expr;

/**
 * CBoolean is a custom boolean expression
 * 
 * @author tiwe
 *
 */
public abstract class CBoolean extends EBoolean implements Custom<Boolean> {
    
    @Override
    public void accept(Visitor v){
        v.visit(this);
    }
    
    @Override
    public Expr<?> getArg(int index) {
        return getArgs().get(index);
    }
    
    // TODO : factory method
}