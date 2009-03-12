/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections15.IteratorUtils;
import org.apache.commons.collections15.Predicate;
import org.apache.commons.collections15.Transformer;
import org.codehaus.janino.ExpressionEvaluator;

import com.mysema.query.grammar.JavaOps;
import com.mysema.query.grammar.types.Expr;
import com.mysema.query.grammar.types.Expr.EBoolean;

/**
 * QueryIteratorUtils provides
 *
 * @author tiwe
 * @version $Id$
 */
public class QueryIteratorUtils {
   
    public static <T> T evaluate(ExpressionEvaluator ev, Object... args){
        try {
            return (T) ev.evaluate(args);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static <S> Iterator<S> multiArgFilter(JavaOps ops, Iterator<S> source, List<Expr<?>> sources, EBoolean condition){
        ExpressionEvaluator ev = EvaluatorUtils.create(ops, sources, condition);
        return multiArgFilter(source, ev);
    }
    
    private static <S> Iterator<S> multiArgFilter(Iterator<S> source, final ExpressionEvaluator ev){
        return IteratorUtils.filteredIterator(source, new Predicate<S>(){
            public boolean evaluate(S object) {
                return QueryIteratorUtils.<Boolean>evaluate(ev, (Object[])object);
            }            
        });
    }
    
    public static <S,T> Iterator<T> project(JavaOps ops, Iterator<S> source, List<Expr<?>> sources, Expr<?> projection){
        ExpressionEvaluator ev = EvaluatorUtils.create(ops, sources, projection);
        return project(source, ev);
    }
    
    private static <S,T> Iterator<T> project(Iterator<S> source, final ExpressionEvaluator ev){
        return IteratorUtils.transformedIterator(source, new Transformer<S,T>(){
            public T transform(S input) {
                return QueryIteratorUtils.<T>evaluate(ev, (Object[])input);
            }            
        });
    }
        
    public static <S> Iterator<S> singleArgFilter(Iterator<S> source, final ExpressionEvaluator ev){
        return IteratorUtils.filteredIterator(source, new Predicate<S>(){
            public boolean evaluate(S object) {
                return QueryIteratorUtils.<Boolean>evaluate(ev, object);
            }            
        });
    }
    
    private static <S> S[] toArray(S... args){
        return args;
    }
    
    public static <S> Iterator<S[]> toArrayIterator(Iterator<S> source){
        return IteratorUtils.transformedIterator(source, new Transformer<S,S[]>(){
            public S[] transform(S input) {
                return toArray(input);
            }
        });
    }

}
