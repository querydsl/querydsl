package com.querydsl.core.support;

import java.util.List;
import java.util.Optional;

import com.mysema.commons.lang.CloseableIterator;
import com.mysema.commons.lang.IteratorAdapter;
import com.querydsl.core.Fetchable;
import com.querydsl.core.NonUniqueResultException;
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

    @Override
    public Optional<T> fetchFirst() {
        return results.isEmpty() ? Optional.empty() : Optional.ofNullable(results.get(0));
    }

    @Override
    public Optional<T> fetchOne() throws NonUniqueResultException {
        if (results.size() > 1) {
            throw new NonUniqueResultException();
        } else if (results.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.ofNullable(results.get(0));
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
