package com.querydsl.core.support;

import java.util.List;

import javax.annotation.Nullable;

import com.mysema.commons.lang.CloseableIterator;
import com.mysema.commons.lang.IteratorAdapter;
import com.querydsl.core.NonUniqueResultException;
import com.querydsl.core.Fetchable;
import com.querydsl.core.QueryModifiers;
import com.querydsl.core.QueryResults;

public class DummyFetchable<T> implements Fetchable<T> {

    private final List<T> results;

    public DummyFetchable(List<T> results) {
        this.results = results;
    }

    @Override
    public CloseableIterator<T> iterate() {
        return new IteratorAdapter<T>(results.iterator());
    }

    @Override
    public List<T> fetch() {
        return results;
    }

    @Nullable
    @Override
    public T fetchFirst() {
        return results.isEmpty() ? null : results.get(0);
    }

    @Nullable
    @Override
    public T fetchOne() {
        if (results.size() > 1) {
            throw new NonUniqueResultException();
        } else if (results.isEmpty()) {
            return null;
        } else {
            return results.get(0);
        }
    }

    @Override
    public QueryResults<T> fetchResults() {
        return new QueryResults<T>(results, QueryModifiers.EMPTY, results.size());
    }

    @Override
    public long fetchCount() {
        return results.size();
    }
}
