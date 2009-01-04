/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mysema.query.grammar.JavaOps;
import com.mysema.query.grammar.OrderSpecifier;
import com.mysema.query.grammar.types.Constructor;
import com.mysema.query.grammar.types.Expr;
import com.mysema.query.grammar.types.Path;
import com.mysema.query.serialization.OperationPatterns;

/**
 * ColQuery is a Query implementation for querying on Java collections
 * 
 * @author tiwe
 * @version $Id$
 */
public class ColQuery<S extends ColQuery<S>>{

    private static final OperationPatterns OPS_DEFAULT = new JavaOps();

    private final InnerQuery query;

    public ColQuery() {
        this(OPS_DEFAULT);
    }

    public ColQuery(OperationPatterns ops) {
        query = new InnerQuery(ops);
    }
    
    private <A> A[] asArray(A[] target, A first, A second, A... rest) {
        target[0] = first;
        target[1] = second;
        System.arraycopy(rest, 0, target, 2, rest.length);
        return target;
    }

    public <A> S from(Path<A> entity, A first, A... rest) {
        List<A> list = new ArrayList<A>(rest.length + 1);
        list.add(first);
        list.addAll(Arrays.asList(rest));
        return from(entity, list);
    }

    @SuppressWarnings("unchecked")
    public <A> S from(Path<A> path, Iterable<A> col) {
        query.alias(path, col).from(path);
        return (S)this;
    }
    
    public <RT> Iterable<RT[]> iterate(Expr<RT> e1, Expr<RT> e2, Expr<RT>... rest) {
        final Expr<RT>[] full = asArray(new Expr[rest.length + 2], e1, e2, rest);
        return query.iterate(new Constructor.CArray<RT>(e1.getType(), full));
    }    
    public <RT> Iterable<RT> iterate(Expr<RT> projection) {
        return query.iterate(projection);
    }
    
    @SuppressWarnings("unchecked")
    public S orderBy(OrderSpecifier<?>... o) {
        query.orderBy(o);
        return (S)this;
    }
    
    @SuppressWarnings("unchecked")
    public S where(Expr.EBoolean o) {
        query.where(o);
        return (S)this;
    }
    
    

}
