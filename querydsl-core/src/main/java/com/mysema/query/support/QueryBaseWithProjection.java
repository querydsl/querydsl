/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.support;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections15.IteratorUtils;

import com.mysema.query.Projectable;
import com.mysema.query.QueryBase;
import com.mysema.query.QueryMetadata;
import com.mysema.query.QueryModifiers;
import com.mysema.query.SearchResults;
import com.mysema.query.types.expr.Expr;

/**
 * QueryBaseWithProjection extends the QueryBase class to provide default
 * implementations of the methods of the Projectable interface
 * 
 * @author tiwe
 * @version $Id$
 */
public abstract class QueryBaseWithProjection<SubType extends QueryBaseWithProjection<SubType>>
        extends QueryBase<SubType> implements Projectable {

    public QueryBaseWithProjection() {
    }

    public QueryBaseWithProjection(QueryMetadata metadata) {
        super(metadata);
    }

    protected <A> A[] asArray(A[] target, A first, A second, A... rest) {
        target[0] = first;
        target[1] = second;
        System.arraycopy(rest, 0, target, 2, rest.length);
        return target;
    }

    public long countDistinct() {
        getMetadata().setDistinct(true);
        return count();
    }

    public SubType limit(long limit) {
        getMetadata().setLimit(limit);
        return _this;
    }

    public SubType offset(long offset) {
        getMetadata().setOffset(offset);
        return _this;
    }

    public SubType restrict(QueryModifiers modifiers) {
        getMetadata().setModifiers(modifiers);
        return _this;
    }

    public final Iterator<Object[]> iterateDistinct(Expr<?> first,
            Expr<?> second, Expr<?>... rest) {
        getMetadata().setDistinct(true);
        return iterate(first, second, rest);
    }

    public final <RT> Iterator<RT> iterateDistinct(Expr<RT> projection) {
        getMetadata().setDistinct(true);
        return iterate(projection);
    }

    public List<Object[]> list(Expr<?> first, Expr<?> second, Expr<?>... rest) {
        return IteratorUtils.toList(iterate(first, second, rest));
    }

    public <RT> List<RT> list(Expr<RT> projection) {
        return IteratorUtils.toList(iterate(projection));
    }

    public final List<Object[]> listDistinct(Expr<?> first, Expr<?> second,
            Expr<?>... rest) {
        getMetadata().setDistinct(true);
        return list(first, second, rest);
    }

    public final <RT> List<RT> listDistinct(Expr<RT> projection) {
        getMetadata().setDistinct(true);
        return list(projection);
    }

    public final <RT> SearchResults<RT> listDistinctResults(Expr<RT> projection){
        getMetadata().setDistinct(true);
        return listResults(projection);
    }
    
    public Object[] uniqueResult(Expr<?> first, Expr<?> second, Expr<?>... rest) {
        getMetadata().setUnique(true);
        limit(1l);
        Iterator<Object[]> it = iterate(first, second, rest);
        return it.hasNext() ? it.next() : null;
    }
    
    public <RT> RT uniqueResult(Expr<RT> expr) {
        getMetadata().setUnique(true);
        limit(1l);
        Iterator<RT> it = iterate(expr);
        return it.hasNext() ? it.next() : null;
    }
}
