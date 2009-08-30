/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.expr;

import com.mysema.query.types.Visitor;

/**
 * ENumberConst represents numeric constants
 * 
 * @author tiwe
 *
 * @param <D>
 */
public class ENumberConst<D extends Number & Comparable<?>> extends ENumber<D> implements Constant<D>{

    private final D constant;
    
    ENumberConst(Class<? extends D> type, D constant) {
        super(type);
        this.constant = constant;
    }

    @Override
    public EBoolean eq(D b){
        return EBoolean.create(constant.equals(b));
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object o) {
        return o instanceof Constant ? ((Constant<?>) o).getConstant().equals(constant) : false;
    }
    
    @Override
    public D getConstant() {
        return constant;
    }
    
    @Override
    public int hashCode() {
        return constant.hashCode();
    }
    
    @Override
    public EBoolean ne(D b){
        return EBoolean.create(!constant.equals(b));
    }
    
    @Override
    public void accept(Visitor v) {
        v.visit(this);        
    }
    
}
