/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import java.util.Arrays;
import java.util.List;

import com.mysema.query.grammar.Grammar;
import com.mysema.query.grammar.GrammarWithAlias;
import com.mysema.query.grammar.OrderSpecifier;
import com.mysema.query.grammar.types.Expr;
import com.mysema.query.grammar.types.SinglePathExtractor;

/**
 * MiniApi provides static convenience methods for query construction
 *
 * @author tiwe
 * @version $Id$
 */
public class MiniApi extends GrammarWithAlias{
    
    public static <A> ColQuery from(Expr<A> path, A... arr){
        return new ColQuery().from(path, Arrays.asList(arr));
    }

    public static <A> ColQuery from(Expr<A> path, Iterable<A> col){
        return new ColQuery().from(path, col);
    }
    
    public static <A> ColQuery from(A alias, Iterable<A> col){
        return new ColQuery().from($(alias), col);
    }
    
    @SuppressWarnings("unchecked")
    public static <A> List<A> select(Iterable<A> from, Expr.EBoolean where, OrderSpecifier<?>... order){
        Expr<A> path = (Expr<A>) new SinglePathExtractor().handle(where).getPath();
        ColQuery query = new ColQuery().from(path, from).where(where).orderBy(order);
        return query.list((Expr<A>)path);
    }
                
    public static <A> List<A> reject(Iterable<A> from, Expr.EBoolean where, OrderSpecifier<?>...order){
        return select(from, Grammar.not(where), order);
    }
             
}
