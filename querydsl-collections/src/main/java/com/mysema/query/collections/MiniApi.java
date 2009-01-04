/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import java.util.Arrays;
import java.util.Map;

import com.mysema.query.grammar.Grammar;
import com.mysema.query.grammar.OrderSpecifier;
import com.mysema.query.grammar.types.Expr;
import com.mysema.query.grammar.types.Path;
import com.mysema.query.grammar.types.PathExtractor;
import com.mysema.query.grammar.types.PathMetadata;
import com.mysema.query.grammar.types.ColTypes.ExtString;
import com.mysema.query.grammar.types.Path.PSimple;

/**
 * MiniApi provides static convenience methods for query construction
 *
 * @author tiwe
 * @version $Id$
 */
public class MiniApi {
    
    private static final ExprFactory exprFactory = new ExprFactory();
    
    private static final Path.PSimple<Object> it = new Path.PSimple<Object>(Object.class,PathMetadata.forVariable("it"));
    
    public static <A> ColQuery<?> from(Path<A> path, A... arr){
        return from(path, Arrays.asList(arr));
    }
    
    public static <K,V> ColQuery<?> from(Path<K> k, Path<V> v, Map<K,V> map){
        return new ColQuery().from(k,v,map);
    }
    
    @SuppressWarnings("unchecked")
    public static <A> ColQuery<?> from(Path<A> path, Iterable<A> col){
        return new ColQuery().from(path, col);
    }
            
    @SuppressWarnings("unchecked")
    public static <A> Iterable<A> select(Iterable<A> from, Expr.EBoolean where, OrderSpecifier<?>... order){
        Path<A> path = (Path<A>) new PathExtractor().handle(where).getPath();
        ColQuery query = new ColQuery().from(path, from).where(where).orderBy(order);
        return query.iterate((Expr<A>)path);
    }
                
    public static <A> Iterable<A> reject(Iterable<A> from, Expr.EBoolean where, OrderSpecifier<?>...order){
        return select(from, Grammar.not(where), order);
    }
    
    public static Path.PBoolean $(Boolean arg){
        return exprFactory.create(arg);
    }
    
    public static <D extends Comparable<D>> Path.PComparable<D> $(D arg){
        return exprFactory.create(arg);
    }
    
    public static ExtString $(String arg){
        return exprFactory.create(arg);
    }

    public static Path.PBooleanArray $(Boolean[] args){
        return exprFactory.create(args);
    }
    
    public static <D extends Comparable<D>> Path.PComparableArray<D> $(D[] args){
        return exprFactory.create(args);
    }
    
    public static Path.PStringArray $(String[] args){
        return exprFactory.create(args);
    }
    
    public static <D> Path.PSimple<D> $(D arg){
        return exprFactory.create(arg);
    }
    
    @SuppressWarnings("unchecked")
    public static <D> Path.PSimple<D> $(){
        return (PSimple<D>) it;
    }
    

     
}
