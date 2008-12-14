/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import com.mysema.query.grammar.Grammar;
import com.mysema.query.grammar.OrderSpecifier;
import com.mysema.query.grammar.types.Expr;
import com.mysema.query.grammar.types.Path;
import com.mysema.query.grammar.types.PathExtractor;
import com.mysema.query.grammar.types.PathMetadata;

/**
 * MiniApi provides static convenience methods for query construction
 *
 * @author tiwe
 * @version $Id$
 */
public class MiniApi {
    
    private static final PathMetadata<String> pm = PathMetadata.forVariable("it");
    
    public static final Path<Object> it = new Path.Entity<Object>(Object.class,"it");
    
    public static final Path.Comparable<Byte> byteVal = new Path.Comparable<Byte>(Byte.class, pm);
    public static final Path.Comparable<Integer> intVal = new Path.Comparable<Integer>(Integer.class, pm);   
    public static final Path.Comparable<Long> longVal = new Path.Comparable<Long>(Long.class, pm);
    public static final Path.Comparable<Float> floatVal = new Path.Comparable<Float>(Float.class, pm);
    public static final Path.Comparable<Double> doubleVal = new Path.Comparable<Double>(Double.class, pm);
    
    public static final Path.String stringVal = new Path.String(pm), str = stringVal;
    
    @SuppressWarnings("unchecked")
    public static <A> Iterable<A> select(Iterable<A> from, Expr.Boolean where, OrderSpecifier<?>... order){
        Path<?> path = new PathExtractor().handle(where).getPath();
        ColQuery query = new ColQuery().from(path, from).where(where).orderBy(order);
        return query.iterate((Expr<A>)path);
    }
                
    public static <A> Iterable<A> reject(Iterable<A> from, Expr.Boolean where, OrderSpecifier<?>...order){
        return select(from, Grammar.not(where), order);
    }
     
}
