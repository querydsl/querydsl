/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.expr;

import com.mysema.query.types.Visitor;


/**
 * ExprConst represents general constant expressions
 * 
 * @author tiwe
 * 
 * @param <D> Java type of constant
 */
@SuppressWarnings("serial")
public class ExprConst<D> extends Expr<D> implements Constant<D> {
    
    private final D constant;

    @SuppressWarnings("unchecked")
    ExprConst(D constant) {
        super((Class<D>) constant.getClass());
        this.constant = constant;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object o) {
        return o instanceof Constant ? ((Constant<?>) o).getConstant().equals(constant) : false;
    }
    
    @Override
    public int hashCode() {
        return constant.hashCode();
    }
    
    /**
     * Get the embedded constant
     * 
     * @return
     */
    @Override
    public D getConstant() {
        return constant;
    }

    @Override
    public EBoolean eq(D s){
        return EBoolean.__create(constant.equals(s));
    }
    
    @Override
    public EBoolean ne(D s){
        return EBoolean.__create(!constant.equals(s));
    }
    
    @Override
    public void accept(Visitor v) {
        v.visit(this);        
    }
    
}