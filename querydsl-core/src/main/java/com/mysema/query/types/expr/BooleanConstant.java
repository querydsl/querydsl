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
public final class BooleanConstant extends BooleanExpression implements Constant<Boolean>{

    public static final BooleanExpression FALSE = new BooleanConstant(Boolean.FALSE);

    private static final long serialVersionUID = -4106376704553234781L;

    public static final BooleanExpression TRUE = new BooleanConstant(Boolean.TRUE);

    public static BooleanExpression create(Boolean b){
        return b.booleanValue() ? TRUE : FALSE;
    }

    private final Boolean constant;

    private BooleanConstant(Boolean b){
        this.constant = b;
    }

    @Override
    public <R,C> R accept(Visitor<R,C> v, C context) {
        return v.visit(this, context);
    }

    @Override
    public BooleanExpression eq(Boolean b){
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
    public BooleanExpression ne(Boolean b){
        return constant.equals(b) ? FALSE : TRUE;
    }

    @Override
    public BooleanExpression not() {
        return constant.booleanValue() ? FALSE : TRUE;
    }

}
