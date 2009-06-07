/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.support;

import java.util.Iterator;
import java.util.List;

import com.mysema.commons.lang.Assert;
import com.mysema.query.Projectable;
import com.mysema.query.SearchResults;
import com.mysema.query.types.expr.Expr;

/**
 * ProjectableAdapter is an adapter implementation for the Projectable interface
 * 
 * @author tiwe
 * @version $Id$
 */
public abstract class ProjectableAdapter implements Projectable {

    private Projectable projectable;

    public ProjectableAdapter() {
    }

    public ProjectableAdapter(Projectable projectable) {
        this.projectable = Assert.notNull(projectable);
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
    public Iterator<Object[]> iterate(Expr<?> first, Expr<?> second, Expr<?>... rest) {
        return projectable.iterate(first, second, rest);
    }

    @Override
    public Iterator<Object[]> iterateDistinct(Expr<?> first, Expr<?> second, Expr<?>... rest) {
        return projectable.iterateDistinct(first, second, rest);
    }

    @Override
    public <RT> Iterator<RT> iterate(Expr<RT> projection) {
        return projectable.iterate(projection);
    }
    
    @Override
    public <RT> Iterator<RT> iterateDistinct(Expr<RT> projection) {
        return projectable.iterateDistinct(projection);
    }

    @Override
    public List<Object[]> list(Expr<?> first, Expr<?> second, Expr<?>... rest) {
        return projectable.list(first, second, rest);
    }

    @Override
    public List<Object[]> listDistinct(Expr<?> first, Expr<?> second, Expr<?>... rest) {
        return projectable.listDistinct(first, second, rest);
    }

    @Override
    public <RT> List<RT> list(Expr<RT> projection) {
        return projectable.list(projection);
    }

    @Override
    public <RT> SearchResults<RT> listResults(Expr<RT> expr) {
        return projectable.listResults(expr);
    }
    
    @Override
    public <RT> SearchResults<RT> listDistinctResults(Expr<RT> expr) {
        return projectable.listDistinctResults(expr);
    }

    @Override
    public <RT> List<RT> listDistinct(Expr<RT> projection) {
        return projectable.listDistinct(projection);
    }

    @Override
    public <RT> RT uniqueResult(Expr<RT> expr) {
        return projectable.uniqueResult(expr);
    }
    
    @Override
    public Object[] uniqueResult(Expr<?> first, Expr<?> second, Expr<?>... rest) {
        return projectable.uniqueResult(first, second, rest);
    }

    public void setProjectable(Projectable projectable) {
        this.projectable = Assert.notNull(projectable);
    }

    public String toString() {
        return projectable.toString();
    }

}
