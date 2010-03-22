/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.support;

import java.util.List;
import java.util.Map;

import com.mysema.commons.lang.Assert;
import com.mysema.commons.lang.CloseableIterator;
import com.mysema.query.Projectable;
import com.mysema.query.SearchResults;
import com.mysema.query.types.Expr;

/**
 * ProjectableAdapter is an adapter implementation for the Projectable interface
 * 
 * @author tiwe
 * @version $Id$
 */
public class ProjectableAdapter<P extends Projectable> implements Projectable {

    private final P projectable;

    public ProjectableAdapter(P projectable) {
        this.projectable = Assert.notNull(projectable,"projectable is null");
    }
    
    protected P getProjectable(){
        return projectable;
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
    public CloseableIterator<Object[]> iterate(Expr<?> first, Expr<?> second, Expr<?>... rest) {
        return projectable.iterate(first, second, rest);
    }

    @Override
    public CloseableIterator<Object[]> iterate(Expr<?>[] args) {
        return projectable.iterate(args);
    }

    @Override
    public <RT> CloseableIterator<RT> iterate(Expr<RT> projection) {
        return projectable.iterate(projection);
    }
    
    @Override
    public CloseableIterator<Object[]> iterateDistinct(Expr<?> first, Expr<?> second, Expr<?>... rest) {
        return projectable.iterateDistinct(first, second, rest);
    }

    @Override
    public CloseableIterator<Object[]> iterateDistinct(Expr<?>[] args) {
        return projectable.iterateDistinct(args);
    }

    @Override
    public <RT> CloseableIterator<RT> iterateDistinct(Expr<RT> projection) {
        return projectable.iterateDistinct(projection);
    }

    @Override
    public List<Object[]> list(Expr<?> first, Expr<?> second, Expr<?>... rest) {
        return projectable.list(first, second, rest);
    }

    @Override
    public List<Object[]> list(Expr<?>[] args) {
        return projectable.list(args);
    }
    
    @Override
    public <RT> List<RT> list(Expr<RT> projection) {
        return projectable.list(projection);
    }

    @Override
    public List<Object[]> listDistinct(Expr<?> first, Expr<?> second, Expr<?>... rest) {
        return projectable.listDistinct(first, second, rest);
    }

    @Override
    public List<Object[]> listDistinct(Expr<?>[] args) {
        return projectable.listDistinct(args);
    }
    
    @Override
    public <RT> List<RT> listDistinct(Expr<RT> projection) {
        return projectable.listDistinct(projection);
    }

    @Override
    public <RT> SearchResults<RT> listDistinctResults(Expr<RT> expr) {
        return projectable.listDistinctResults(expr);
    }

    @Override
    public <RT> SearchResults<RT> listResults(Expr<RT> expr) {
        return projectable.listResults(expr);
    }

    @Override
    public <K, V> Map<K, V> map(Expr<K> key, Expr<V> value) {
        return projectable.map(key, value);
    }

    public String toString() {
        return projectable.toString();
    }

    @Override
    public Object[] uniqueResult(Expr<?> first, Expr<?> second, Expr<?>... rest) {
        return projectable.uniqueResult(first, second, rest);
    }

    @Override
    public Object[] uniqueResult(Expr<?>[] args) {
        return projectable.uniqueResult(args);
    }

    @Override
    public <RT> RT uniqueResult(Expr<RT> expr) {
        return projectable.uniqueResult(expr);
    }

}
