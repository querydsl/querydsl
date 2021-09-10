package com.querydsl.core.support;

import java.util.List;

import com.mysema.commons.lang.CloseableIterator;
import com.querydsl.core.*;
import org.jetbrains.annotations.Nullable;

public class DummyFetchable<T> implements Fetchable<T> {

    private final List<T> results;

    public DummyFetchable(List<T> results) {
        this.results = results;
    }

    @Override
    public CloseableIterator<T> iterate() {
        return CloseableIterator.fromIterator(results.iterator());
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
    public T fetchOne() throws NonUniqueResultException {
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
