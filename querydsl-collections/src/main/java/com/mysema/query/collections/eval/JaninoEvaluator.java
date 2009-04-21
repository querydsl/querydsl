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
 * JaninoEvaluator is a Janino compiler based Evaluator implementation
 *
 * @author tiwe
 * @version $Id$
 */
public class JaninoEvaluator implements Evaluator{

    private final ExpressionEvaluator ev;
    
    private final Expr<?> projection;
    
    private final List<? extends Expr<?>> sources;
    
    public JaninoEvaluator(ExpressionEvaluator ev, List<? extends Expr<?>> sources, Expr<?> projection){
        this.ev = Assert.notNull(ev);
        this.sources = sources;
        this.projection = projection;        
    }
    
    public JaninoEvaluator(JavaOps ops, List<? extends Expr<?>> sources, Expr<?> expr){
        try {
            Class<?> type = expr.getType() != null ? expr.getType() : Object.class;
            this.ev = new JavaSerializer(ops).handle(expr).createExpressionEvaluator(sources, type);
            this.sources = sources;
            this.projection = expr;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    @SuppressWarnings("unchecked")
    public <T> T evaluate(Object... args) {
        try {
            return (T)ev.evaluate(args);
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof NullPointerException){
                for (int i=0; i < args.length; i++){
                    if (args[i] == null){
                        throw new IllegalArgumentException("null for " + sources.get(i));        
                    }
                }
                throw new IllegalArgumentException("null in " + projection);
            }else{
                throw new RuntimeException(e.getMessage(), e);    
            }            
        }
    }

}
