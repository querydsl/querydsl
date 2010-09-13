/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types.expr;

import java.util.ArrayList;
import java.util.List;

import com.mysema.query.types.Expression;
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
public class Coalesce<T extends Comparable> extends ComparableExpression<T>{

    private static final long serialVersionUID = 445439522266250417L;

    private final List<Expression<? extends T>> exprs = new ArrayList<Expression<? extends T>>();

    public Coalesce(Class<? extends T> type, Expression...exprs){
        // NOTE : type parameters for the varargs, would result in compiler warnings
        super(type);
        for (Expression expr : exprs){
            add(expr);
        }
    }

    public Coalesce(Expression... exprs){
        // NOTE : type parameters for the varargs, would result in compiler warnings
        this((Class<T>)(exprs.length > 0 ? exprs[0].getType() : Object.class), exprs);
    }

    @Override
    public <R,C> R accept(Visitor<R,C> v, C context) {
        return SimpleOperation.create(getType(), Ops.COALESCE, getExpressionList()).accept(v, context);
    }

    public final Coalesce<T> add(Expression<T> expr){
        this.exprs.add(expr);
        return this;
    }

    public final Coalesce<T> add(T constant){
        this.exprs.add(SimpleConstant.create(constant));
        return this;
    }

    public DateExpression<T> asDate(){
        return (DateExpression<T>) DateOperation.create(getType(), Ops.COALESCE, getExpressionList());
    }

    public DateTimeExpression<T> asDateTime(){
        return (DateTimeExpression<T>) DateTimeOperation.create(getType(), Ops.COALESCE, getExpressionList());
    }

    public NumberExpression<?> asNumber(){
        return NumberOperation.create((Class)getType(), Ops.COALESCE, getExpressionList());
    }

    public StringExpression asString(){
        return StringOperation.create(Ops.COALESCE, getExpressionList());
    }

    public TimeExpression<T> asTime(){
        return (TimeExpression<T>) TimeOperation.create(getType(), Ops.COALESCE, getExpressionList());
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

    private Expression<?> getExpressionList(){
        Expression<?> arg = exprs.get(0);
        for (int i = 1; i < exprs.size(); i++){
            arg = SimpleOperation.create(List.class, Ops.LIST, arg, exprs.get(i));
        }
        return arg;
    }

    @Override
    public int hashCode(){
        return exprs.hashCode();
    }

}
