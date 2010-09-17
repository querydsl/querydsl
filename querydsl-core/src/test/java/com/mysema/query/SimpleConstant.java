/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query;

import com.mysema.commons.lang.Assert;
import com.mysema.query.types.Constant;
import com.mysema.query.types.Visitor;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.expr.SimpleExpression;

/**
 * SimpleConstant represents general constant expressions
 *
 * @author tiwe
 *
 * @param <D> Java type of constant
 */
public class SimpleConstant<D> extends SimpleExpression<D> implements Constant<D> {

    private static final long serialVersionUID = -3211963259241932307L;

    /**
     * Factory method for constants
     *
     * @param <D>
     * @param val
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> SimpleExpression<T> create(T val){
        if (val instanceof Boolean){
            return (SimpleExpression<T>)BooleanConstant.create((Boolean)val);
        }else{
            return new SimpleConstant<T>(Assert.notNull(val,"val"));
        }
    }

    private final D constant;

    @SuppressWarnings("unchecked")
    SimpleConstant(D constant) {
        super((Class<D>) constant.getClass());
        this.constant = constant;
    }

    @Override
    public <R,C> R accept(Visitor<R,C> v, C context) {
        return v.visit(this, context);
    }

    @Override
    public BooleanExpression eq(D s){
        return BooleanConstant.create(constant.equals(s));
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object o) {
        if (o == this){
            return true;
        }else if (o instanceof Constant){
            return ((Constant)o).getConstant().equals(constant);
        }else{
            return false;
        }
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
    public int hashCode() {
        return constant.hashCode();
    }

    @Override
    public BooleanExpression ne(D s){
        return BooleanConstant.create(!constant.equals(s));
    }

}
