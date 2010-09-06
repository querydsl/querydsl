/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types.expr;

import com.mysema.query.types.Constant;
import com.mysema.query.types.Visitor;

/**
 * EBooleanConst provides constants for Boolean.TRUE and Boolean.FALSE
 *
 * @author tiwe
 *
 */
public final class EBooleanConst extends EBoolean implements Constant<Boolean>{

    public static final EBoolean FALSE = new EBooleanConst(Boolean.FALSE);

    private static final long serialVersionUID = -4106376704553234781L;

    public static final EBoolean TRUE = new EBooleanConst(Boolean.TRUE);

    public static EBoolean create(Boolean b){
        return b.booleanValue() ? TRUE : FALSE;
    }

    private final Boolean constant;

    private EBooleanConst(Boolean b){
        this.constant = b;
    }

    @Override
    public <R,C> R accept(Visitor<R,C> v, C context) {
        return v.visit(this, context);
    }

    @Override
    public EBoolean eq(Boolean b){
        return constant.equals(b) ? TRUE : FALSE;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object o) {
        if (o == this){
            return true;
        }else if (o instanceof Constant){
            Constant c = (Constant)o;
            return c.getConstant().equals(constant);
        }else{
            return false;
        }
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

}
