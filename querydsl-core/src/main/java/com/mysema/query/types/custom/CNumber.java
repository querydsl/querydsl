/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.custom;

import com.mysema.query.types.Visitor;
import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.expr.Expr;

/**
 * CNumber defines custom numeric expressions
 * 
 * @author tiwe
 *
 * @param <T>
 */
public abstract class CNumber<T extends Number & Comparable<?>> extends ENumber<T> implements Custom<T> {
    
    public CNumber(Class<T> type) {
        super(type);
    }
    
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