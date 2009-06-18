/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import java.util.Arrays;
import java.util.List;

import com.mysema.query.alias.GrammarWithAlias;
import com.mysema.query.types.Grammar;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.SinglePathExtractor;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.Expr;

/**
 * MiniApi provides static convenience methods for query construction
 * 
 * @author tiwe
 * @version $Id$
 */
// TODO : rename to MiniAPI
public class MiniApi extends GrammarWithAlias {

    public static <A> ColQuery from(Expr<A> path, A... arr) {
        return new ColQueryImpl().from(path, Arrays.asList(arr));
    }

    public static <A> ColQuery from(Expr<A> path, Iterable<A> col) {
        return new ColQueryImpl().from(path, col);
    }

    public static <A> ColQuery from(A alias, Iterable<A> col) {
        return new ColQueryImpl().from($(alias), col);
    }

    @SuppressWarnings("unchecked")
    public static <A> List<A> select(Iterable<A> from, EBoolean where, OrderSpecifier<?>... order) {
        Expr<A> path = (Expr<A>) new SinglePathExtractor().handle(where).getPath();
        ColQuery query = new ColQueryImpl().from(path, from).where(where).orderBy(order);
        return query.list((Expr<A>) path);
    }

    public static <A> List<A> reject(Iterable<A> from, EBoolean where, OrderSpecifier<?>... order) {
        return select(from, where.not(), order);
    }

}
