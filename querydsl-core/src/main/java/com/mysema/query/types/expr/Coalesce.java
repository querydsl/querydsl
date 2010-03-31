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
public class Coalesce<T> extends ESimple<T>{
    
    private static final long serialVersionUID = 445439522266250417L;
    
    private final List<Expr<? extends T>> exprs = new ArrayList<Expr<? extends T>>();
    
    public Coalesce(Class<? extends T> type, Expr<T> ...exprs){
        super(type);
        add(exprs);
    }

    @SuppressWarnings("unchecked")
    public Coalesce(Expr<T> ...exprs){
        this((Class<T>)(exprs.length > 0 ? exprs[0].getType() : Object.class), exprs);
    }
    
    public Coalesce<T> add(Expr<T>... exprs){
        for (Expr<T> expr : exprs){
            this.exprs.add(expr);
        }
        return this;
    }
    
    public Coalesce<T> add(T constant){
        this.exprs.add(ExprConst.create(constant));
        return this;
    }
        
    @Override
    public void accept(Visitor v) {
        Expr<?> arg = exprs.get(0);
        for (int i = 1; i < exprs.size(); i++){
            arg = OSimple.create(List.class, Ops.LIST, arg, exprs.get(i));
        }        
        OSimple.create(getType(), Ops.COALESCE, arg).accept(v);
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

}
