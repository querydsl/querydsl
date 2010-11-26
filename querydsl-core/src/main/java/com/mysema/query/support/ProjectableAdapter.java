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
import com.mysema.query.types.Expression;

/**
 * ProjectableAdapter is an adapter implementation for the Projectable interface
 *
 * @author tiwe
 * @version $Id$
 */
public class ProjectableAdapter<P extends Projectable> implements Projectable {

    private final P projectable;

    public ProjectableAdapter(P projectable) {
        this.projectable = Assert.notNull(projectable,"projectable");
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
    public boolean exists() {
        return projectable.exists();
    }

    @Override
    public boolean notExists() {
        return projectable.notExists();
    }

    @Override
    public CloseableIterator<Object[]> iterate(Expression<?> first, Expression<?> second, Expression<?>... rest) {
        return projectable.iterate(first, second, rest);
    }

    @Override
    public CloseableIterator<Object[]> iterate(Expression<?>[] args) {
        return projectable.iterate(args);
    }

    @Override
    public <RT> CloseableIterator<RT> iterate(Expression<RT> projection) {
        return projectable.iterate(projection);
    }

    @Override
    public CloseableIterator<Object[]> iterateDistinct(Expression<?> first, Expression<?> second, Expression<?>... rest) {
        return projectable.iterateDistinct(first, second, rest);
    }

    @Override
    public CloseableIterator<Object[]> iterateDistinct(Expression<?>[] args) {
        return projectable.iterateDistinct(args);
    }

    @Override
    public <RT> CloseableIterator<RT> iterateDistinct(Expression<RT> projection) {
        return projectable.iterateDistinct(projection);
    }

    @Override
    public List<Object[]> list(Expression<?> first, Expression<?> second, Expression<?>... rest) {
        return projectable.list(first, second, rest);
    }

    @Override
    public List<Object[]> list(Expression<?>[] args) {
        return projectable.list(args);
    }

    @Override
    public <RT> List<RT> list(Expression<RT> projection) {
        return projectable.list(projection);
    }

    @Override
    public List<Object[]> listDistinct(Expression<?> first, Expression<?> second, Expression<?>... rest) {
        return projectable.listDistinct(first, second, rest);
    }

    @Override
    public List<Object[]> listDistinct(Expression<?>[] args) {
        return projectable.listDistinct(args);
    }

    @Override
    public <RT> List<RT> listDistinct(Expression<RT> projection) {
        return projectable.listDistinct(projection);
    }

    @Override
    public <RT> SearchResults<RT> listDistinctResults(Expression<RT> expr) {
        return projectable.listDistinctResults(expr);
    }

    @Override
    public <RT> SearchResults<RT> listResults(Expression<RT> expr) {
        return projectable.listResults(expr);
    }

    @Override
    public <K, V> Map<K, V> map(Expression<K> key, Expression<V> value) {
        return projectable.map(key, value);
    }

    @Override
    public String toString() {
        return projectable.toString();
    }

    @Override
    public Object[] uniqueResult(Expression<?> first, Expression<?> second, Expression<?>... rest) {
        return projectable.uniqueResult(first, second, rest);
    }

    @Override
    public Object[] uniqueResult(Expression<?>[] args) {
        return projectable.uniqueResult(args);
    }

    @Override
    public <RT> RT uniqueResult(Expression<RT> expr) {
        return projectable.uniqueResult(expr);
    }

}
