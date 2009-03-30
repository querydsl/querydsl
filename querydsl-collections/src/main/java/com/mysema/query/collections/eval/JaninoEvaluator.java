/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections.eval;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.codehaus.janino.ExpressionEvaluator;

import com.mysema.query.grammar.JavaOps;
import com.mysema.query.grammar.types.Expr;
import com.mysema.query.util.Assert;

/**
 * JaninoEvaluator provides
 *
 * @author tiwe
 * @version $Id$
 */
public class JaninoEvaluator implements Evaluator{

    private final ExpressionEvaluator ev;
    
    public JaninoEvaluator(ExpressionEvaluator ev){
        this.ev = Assert.notNull(ev);
    }
    
    public JaninoEvaluator(JavaOps ops, List<? extends Expr<?>> sources, Expr<?> expr){
        try {
            Class<?> type = expr.getType() != null ? expr.getType() : Object.class;
            ev = new JavaSerializer(ops).handle(expr).createExpressionEvaluator(sources, type);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    public <T> T evaluate(Object... args) {
        try {
            return (T)ev.evaluate(args);
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof NullPointerException){
                throw new IllegalArgumentException("null path in expression");
            }else{
                throw new RuntimeException(e.getMessage(), e);    
            }            
        }
    }

}
