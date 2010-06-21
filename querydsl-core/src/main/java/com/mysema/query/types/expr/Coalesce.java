/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types.expr;

import java.util.ArrayList;
import java.util.List;

import com.mysema.query.types.Expr;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Visitor;

/**
 * Coalesce defines a coalesce function invocation. The coalesce function
 * returns null if all arguments are null and the first non-null argument
 * otherwise
 *
 * @author tiwe
 *
 * @param <T>
 */
@SuppressWarnings("unchecked")
public class Coalesce<T extends Comparable> extends EComparable<T>{

    private static final long serialVersionUID = 445439522266250417L;

    private final List<Expr<? extends T>> exprs = new ArrayList<Expr<? extends T>>();

    /**
     * @param type
     * @param exprs
     */
    public Coalesce(Class<? extends T> type, Expr...exprs){
        // NOTE : type parameters for the varargs, would result in compiler warnings
        super(type);
        for (Expr expr : exprs){
            add(expr);
        }
    }

    /**
     * @param exprs
     */
    public Coalesce(Expr... exprs){
        // NOTE : type parameters for the varargs, would result in compiler warnings
        this((Class<T>)(exprs.length > 0 ? exprs[0].getType() : Object.class), exprs);
    }

    @Override
    public void accept(Visitor v) {
        OSimple.create(getType(), Ops.COALESCE, getExpressionList()).accept(v);
    }

    public Coalesce<T> add(Expr<T> expr){
        this.exprs.add(expr);
        return this;
    }

    public Coalesce<T> add(T constant){
        this.exprs.add(ExprConst.create(constant));
        return this;
    }

    public EDate<T> asDate(){
        return (EDate<T>) ODate.create(getType(), Ops.COALESCE, getExpressionList());
    }

    public EDateTime<T> asDateTime(){
        return (EDateTime<T>) ODateTime.create(getType(), Ops.COALESCE, getExpressionList());
    }

    public ENumber<?> asNumber(){
        return ONumber.create((Class)getType(), Ops.COALESCE, getExpressionList());
    }

    public EString asString(){
        return OString.create(Ops.COALESCE, getExpressionList());
    }

    public ETime<T> asTime(){
        return (ETime<T>) OTime.create(getType(), Ops.COALESCE, getExpressionList());
    }

    @Override
    public boolean equals(Object o) {
        if (o == this){
            return true;
        }else if (o instanceof Coalesce<?>){
            Coalesce<?> c = (Coalesce<?>)o;
            return c.exprs.equals(exprs);
        }else{
            return false;
        }
    }

    private Expr<?> getExpressionList(){
        Expr<?> arg = exprs.get(0);
        for (int i = 1; i < exprs.size(); i++){
            arg = OSimple.create(List.class, Ops.LIST, arg, exprs.get(i));
        }
        return arg;
    }

    @Override
    public int hashCode(){
        return exprs.hashCode();
    }

}
