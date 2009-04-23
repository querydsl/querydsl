/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar.types;

import java.util.HashMap;
import java.util.Map;

import com.mysema.query.grammar.Ops.Op;
import com.mysema.query.grammar.types.Expr.EBoolean;
import com.mysema.query.grammar.types.Expr.EComparable;
import com.mysema.query.grammar.types.Expr.EConstant;
import com.mysema.query.grammar.types.Expr.ENumber;
import com.mysema.query.grammar.types.Expr.EString;
import com.mysema.query.grammar.types.Operation.OBoolean;
import com.mysema.query.grammar.types.Operation.OComparable;
import com.mysema.query.grammar.types.Operation.ONumber;
import com.mysema.query.grammar.types.Operation.OString;
import com.mysema.query.grammar.types.Operation.OStringArray;
import com.mysema.util.Assert;

/**
 * Factory provides factory methods for various needs
 * 
 * @author tiwe
 * @version $Id$
 */

public class SimpleExprFactory implements ExprFactory{

    private static final ExprFactory instance = new SimpleExprFactory();
    
    private final Map<Class<?>,Expr<?>> classToExpr = new HashMap<Class<?>,Expr<?>>();
    
    @SuppressWarnings("unchecked")
    private final Expr<Integer>[] integers = new Expr[256];
    
    protected SimpleExprFactory(){
        for (int i=0; i < integers.length; i++){
            integers[i] = new EConstant<Integer>(i-128);
        }
    }
    
    public static ExprFactory getInstance(){
        return instance;
    }
    
    public EBoolean createBoolean(Op<Boolean> operator, Expr<?>... args) {
        return new OBoolean(Assert.notNull(operator), Assert.notNull(args));
    }
    
    public <OpType, RT extends Comparable<?>> EComparable<RT> createComparable(Class<RT> type, Op<OpType> operator, Expr<?>... args) {
        return new OComparable<OpType,RT>(type, Assert.notNull(operator), Assert.notNull(args));
    }
    
    @SuppressWarnings("unchecked")
    public <A> Expr<A> createConstant(A obj) {
        if (obj instanceof Expr) {
            return (Expr<A>) obj;
        }else{
            return new EConstant<A>(Assert.notNull(obj));    
        }        
    }
    
    @SuppressWarnings("unchecked")
    public <A> Expr<Class<A>> createConstant(Class<A> obj) {
        if (classToExpr.containsKey(obj)){
            return (Expr<Class<A>>) classToExpr.get(obj);
        }else{
            Expr<Class<A>> expr = new EConstant<Class<A>>(obj);
            classToExpr.put(obj, expr);
            return expr;
        }
    }
    
    public Expr<Integer> createConstant(int i){
        if (i >= -128 && i <= 127) { 
            return integers[i+128];
        }else{
            return new EConstant<Integer>(i);        
        }
    }
    
    public <OpType extends Number,D extends Number & Comparable<?>> ENumber<D> createNumber(Class<? extends D> type, Op<OpType> operator, Expr<?>... args) {
        return new ONumber<OpType,D>(type, Assert.notNull(operator), Assert.notNull(args));
    }

    public EString createString(Op<String> operator, Expr<?>... args) {
        return new OString(Assert.notNull(operator), Assert.notNull(args));
    }

    public Expr<String[]> createStringArray(Op<String> operator, Expr<?>... args) {
        return new OStringArray(Assert.notNull(operator), Assert.notNull(args));
    }

}
