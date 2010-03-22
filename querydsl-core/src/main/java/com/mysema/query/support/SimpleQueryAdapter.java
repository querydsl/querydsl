/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.support;

import java.util.List;

import com.mysema.query.Projectable;
import com.mysema.query.Query;
import com.mysema.query.QueryModifiers;
import com.mysema.query.SearchResults;
import com.mysema.query.SimpleProjectable;
import com.mysema.query.SimpleQuery;
import com.mysema.query.types.Expr;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.expr.EBoolean;

import edu.umd.cs.findbugs.annotations.SuppressWarnings;

/**
 * @author tiwe
 *
 * @param <T>
 */
public class SimpleQueryAdapter<T> implements SimpleQuery<SimpleQueryAdapter<T>>, SimpleProjectable<T>{

    private final Projectable projectable;
    
    private final Expr<T> projection;
    
    private final Query<?> query;

    @SuppressWarnings("BC_UNCONFIRMED_CAST")
    public <Q extends Query<?> & Projectable> SimpleQueryAdapter(Q query, Expr<T> projection){
        // NOTE : this is a correct cast which is not handled properly by FindBugs
        this(query, query, projection);
    }
    
    public SimpleQueryAdapter(Query<?> query, Projectable projectable, Expr<T> projection){
        this.query = query;
        this.projectable = projectable;
        this.projection = projection;
    }

    @Override
    public long count() {
        return projectable.count();
    }

    @Override
    public long countDistinct() {
        return projectable.countDistinct();
    }

    @Override
    public SimpleQueryAdapter<T> limit(long limit) {
        query.limit(limit);
        return this;
    }

    @Override
    public List<T> list() {
        return projectable.list(projection);
    }

    @Override
    public List<T> listDistinct() {
        return projectable.listDistinct(projection);
    }

    @Override
    public SearchResults<T> listDistinctResults() {
        return projectable.listDistinctResults(projection);
    }

    @Override
    public SearchResults<T> listResults() {
        return projectable.listResults(projection);
    }

    @Override
    public SimpleQueryAdapter<T> offset(long offset) {
        query.offset(offset);
        return this;
    }

    @Override
    public SimpleQueryAdapter<T> orderBy(OrderSpecifier<?>... o) {
        query.orderBy(o);
        return this;
    }

    @Override
    public SimpleQueryAdapter<T> restrict(QueryModifiers modifiers) {
        query.restrict(modifiers);
        return this;
    }

    @Override
    public String toString(){
        return query.toString();
    }
    
    @Override
    public T uniqueResult() {
        return projectable.uniqueResult(projection);
    }

    @Override
    public SimpleQueryAdapter<T> where(EBoolean... e) {
        query.where(e);
        return this;
    }
    
}
