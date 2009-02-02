/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import java.util.Arrays;

import com.mysema.query.grammar.Grammar;
import com.mysema.query.grammar.GrammarWithAlias;
import com.mysema.query.grammar.OrderSpecifier;
import com.mysema.query.grammar.types.Expr;
import com.mysema.query.grammar.types.Path;
import com.mysema.query.grammar.types.PathExtractor;

/**
 * MiniApi provides static convenience methods for query construction
 *
 * @author tiwe
 * @version $Id$
 */
public class MiniApi extends GrammarWithAlias{
    
    public static <A> ColQuery<?> from(Expr<A> path, A... arr){
        return from(path, Arrays.asList(arr));
    }
    
    @SuppressWarnings("unchecked")
    public static <A> ColQuery<?> from(A alias, Iterable<A> col){
        return new ColQuery().from($(alias), col);
    }
    
    @SuppressWarnings("unchecked")
    public static <A> ColQuery<?> from(Expr<A> path, Iterable<A> col){
        return new ColQuery().from((Path<?>)path, col);
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
             
}
