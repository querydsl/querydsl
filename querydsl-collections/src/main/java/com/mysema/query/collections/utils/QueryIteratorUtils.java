/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections.utils;

import java.util.*;

import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.IteratorUtils;
import org.apache.commons.collections15.Predicate;
import org.apache.commons.collections15.Transformer;

import com.mysema.query.collections.eval.Evaluator;
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
       
    /**
     * filter the given iterator using the given condition
     * 
     * @param <S>
     * @param ops
     * @param source
     * @param sources
     * @param condition
     * @return
     */
    public static <S> Iterator<S> multiArgFilter(JavaOps ops, Iterator<S> source, List<Expr<?>> sources, EBoolean condition){
        Evaluator ev = EvaluatorUtils.create(ops, sources, condition);
        return multiArgFilter(source, ev);
    }
    
    private static <S> Iterator<S> multiArgFilter(Iterator<S> source, final Evaluator ev){
        return IteratorUtils.filteredIterator(source, new Predicate<S>(){
            public boolean evaluate(S object) {
                return ev.<Boolean>evaluate((Object[])object);
            }            
        });
    }
    
    /**
     * transform the given source iterator using the given projection expression
     * 
     * @param <S>
     * @param <T>
     * @param ops
     * @param source
     * @param sources
     * @param projection
     * @return
     */
    public static <S,T> Iterator<T> transform(JavaOps ops, Iterator<S> source, List<Expr<?>> sources, Expr<?> projection){
        Evaluator ev = EvaluatorUtils.create(ops, sources, projection);
        return transform(source, ev);
    }
    
    private static <S,T> Iterator<T> transform(Iterator<S> source, final Evaluator ev){
        return IteratorUtils.transformedIterator(source, new Transformer<S,T>(){
            public T transform(S input) {
                return ev.<T>evaluate((Object[])input);
            }            
        });
    }
    
    /**
     * project the given source iterator to a map by treating the iterator values 
     * as map values and the projections as map keys
     * 
     * @param <S>
     * @param <T>
     * @param source
     * @param ev
     * @return
     */
    public static <S,T> Map<S,? extends Iterable<T>> projectToMap(Iterator<T> source, Evaluator ev){
        int size = 300;
        if (source instanceof Collection){
            size = (int)Math.ceil(((Collection<?>)source).size() * 0.7);
        }
        Map<S,Collection<T>> map = new HashMap<S,Collection<T>>(size);
        while (source.hasNext()){
            T value = source.next();
            S key = ev.<S>evaluate(value);
            Collection<T> col = map.get(key);
            if (col == null){
                col = new ArrayList<T>();
                map.put(key, col);
            }
            col.add(value);
        }
        return map;
    }
        
    /**
     * filter the given iterator using the given expressionevaluator that evaluates to true / false
     * 
     * @param <S>
     * @param source
     * @param ev
     * @return
     */
    public static <S> Iterator<S> singleArgFilter(Iterator<S> source, final Evaluator ev){
        return IteratorUtils.filteredIterator(source, new Predicate<S>(){
            public boolean evaluate(S object) {
                return ev.<Boolean>evaluate(object);
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
