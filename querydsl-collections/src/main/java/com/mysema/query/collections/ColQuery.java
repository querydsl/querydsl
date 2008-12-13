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
import com.mysema.query.grammar.types.Expr;
import com.mysema.query.grammar.types.Path;
import com.mysema.query.serialization.OperationPatterns;

/**
 * ColQuery is a Query implementation for querying on Java collections
 * 
 * @author tiwe
 * @version $Id$
 */
public class ColQuery<S extends ColQuery<S>> {

    private static final OperationPatterns OPS_DEFAULT = new JavaOps();

    private final InnerQuery query;

    public ColQuery() {
        this(OPS_DEFAULT);
    }

    public ColQuery(OperationPatterns ops) {
        query = new InnerQuery(ops);
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
    
    public Iterable<Object[]> iterate(Expr<?> e1, Expr<?> e2, Expr<?>... rest) {
        return query.iterate(e1, e2, rest);
    }
    
    public <RT> Iterable<RT> iterate(Expr<RT> projection) {
        return query.iterate(projection);
    }
    
    @SuppressWarnings("unchecked")
    public S orderBy(OrderSpecifier<?>... o) {
        query.orderBy(o);
        return (S) this;
    }
    
    @SuppressWarnings("unchecked")
    public S where(Expr.Boolean o) {
        query.where(o);
        return (S)this;
    }

}
