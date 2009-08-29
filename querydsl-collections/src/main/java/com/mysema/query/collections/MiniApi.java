/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import java.util.Arrays;

import com.mysema.query.alias.Alias;
import com.mysema.query.collections.impl.ColQueryImpl;
import com.mysema.query.types.expr.Expr;

/**
 * MiniApi provides static convenience methods for query construction
 * 
 * @author tiwe
 * @version $Id$
 */
public final class MiniApi {
    
    private MiniApi(){}
    
    private static final ColQueryTemplates templates = new ColQueryTemplates();

    public static <A> ColQuery from(Expr<A> path, A... arr) {
        return new ColQueryImpl(templates).from(path, Arrays.asList(arr));
    }

    public static <A> ColQuery from(Expr<A> path, Iterable<A> col) {
        return new ColQueryImpl(templates).from(path, col);
    }

    public static <A> ColQuery from(A alias, Iterable<A> col) {
        return new ColQueryImpl(templates).from(Alias.$(alias), col);
    }

}
