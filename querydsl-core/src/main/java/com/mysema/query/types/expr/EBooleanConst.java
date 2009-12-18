/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.expr;

import com.mysema.query.types.Visitor;

/**
 * EBooleanConst provides constants for Boolean.TRUE and Boolean.FALSE
 * 
 * @author tiwe
 *
 */
@SuppressWarnings("serial")
public class EBooleanConst extends EBoolean implements Constant<Boolean>{
       
    public static final EBoolean FALSE = new EBooleanConst(Boolean.FALSE);
    
    public static final EBoolean TRUE = new EBooleanConst(Boolean.TRUE);
    
    public static final EBoolean create(Boolean b){
        return b.booleanValue() ? TRUE : FALSE;
    }
    
    private final Boolean constant;
    
    EBooleanConst(Boolean b){
        this.constant = b;
    }
    
    @Override
    public EBoolean eq(Boolean b){
        return constant.equals(b) ? TRUE : FALSE;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object o) {
        return o instanceof Constant ? ((Constant<?>) o).getConstant().equals(constant) : false;
    }
    
    @Override
    public Boolean getConstant() {
        return constant;
    }

    @Override
    public int hashCode() {
        return constant.hashCode();
    }
    
    @Override
    public EBoolean ne(Boolean b){
        return constant.equals(b) ? FALSE : TRUE;
    }
    
    @Override
    public EBoolean not() {
        return constant.booleanValue() ? FALSE : TRUE;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);        
    }

}
