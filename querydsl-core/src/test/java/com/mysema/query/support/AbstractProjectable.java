/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.support;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.mysema.commons.lang.CloseableIterator;
import com.mysema.commons.lang.EmptyCloseableIterator;
import com.mysema.query.Projectable;
import com.mysema.query.SearchResults;
import com.mysema.query.types.Expression;

public class AbstractProjectable implements Projectable {

    @Override
    public long count() {
        return 0;
    }

    @Override
    public long countDistinct() {
        return 0;
    }

    @Override
    public boolean exists() {
        return 0 < count();
    }

    @Override
    public boolean notExists() {
        return !exists();
    }

    @Override
    public CloseableIterator<Object[]> iterate(Expression<?> first,
            Expression<?> second, Expression<?>... rest) {
        return null;
    }

    @Override
    public CloseableIterator<Object[]> iterate(Expression<?>[] args) {
        return new EmptyCloseableIterator<Object[]>();
    }

    @Override
    public <RT> CloseableIterator<RT> iterate(Expression<RT> projection) {
        return new EmptyCloseableIterator<RT>();
    }

    @Override
    public CloseableIterator<Object[]> iterateDistinct(Expression<?> first,
            Expression<?> second, Expression<?>... rest) {
        return new EmptyCloseableIterator<Object[]>();
    }

    @Override
    public CloseableIterator<Object[]> iterateDistinct(Expression<?>[] args) {
        return new EmptyCloseableIterator<Object[]>();
    }

    @Override
    public <RT> CloseableIterator<RT> iterateDistinct(Expression<RT> projection) {
        return new EmptyCloseableIterator<RT>();
    }

    @Override
    public List<Object[]> list(Expression<?> first, Expression<?> second,
            Expression<?>... rest) {
        return Collections.emptyList();
    }

    @Override
    public List<Object[]> list(Expression<?>[] args) {
        return Collections.emptyList();
    }

    @Override
    public <RT> List<RT> list(Expression<RT> projection) {
        return Collections.emptyList();
    }

    @Override
    public List<Object[]> listDistinct(Expression<?> first,
            Expression<?> second, Expression<?>... rest) {
        return Collections.emptyList();
    }

    @Override
    public List<Object[]> listDistinct(Expression<?>[] args) {
        return Collections.emptyList();
    }

    @Override
    public <RT> List<RT> listDistinct(Expression<RT> projection) {
        return Collections.emptyList();
    }

    @Override
    public <RT> SearchResults<RT> listResults(Expression<RT> projection) {
        return new SearchResults<RT>(Collections.<RT>emptyList(), null, null, 0l);
    }

    @Override
    public <RT> SearchResults<RT> listDistinctResults(Expression<RT> projection) {
        return new SearchResults<RT>(Collections.<RT>emptyList(), null, null, 0l);
    }

    @Override
    public <K, V> Map<K, V> map(Expression<K> key, Expression<V> value) {
        return Collections.emptyMap();
    }

    @Override
    public Object[] singleResult(Expression<?> first, Expression<?> second,
            Expression<?>... rest) {
        return new Object[0];
    }

    @Override
    public Object[] singleResult(Expression<?>[] args) {
        return null;
    }

    @Override
    public <RT> RT singleResult(Expression<RT> projection) {
        return null;
    }

    @Override
    public Object[] uniqueResult(Expression<?> first, Expression<?> second,
            Expression<?>... rest) {
        return null;
    }

    @Override
    public Object[] uniqueResult(Expression<?>[] args) {
        return null;
    }

    @Override
    public <RT> RT uniqueResult(Expression<RT> projection) {
        return null;
    }

}
