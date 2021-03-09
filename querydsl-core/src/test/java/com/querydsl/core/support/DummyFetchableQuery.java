package com.querydsl.core.support;

import java.util.List;

import org.jetbrains.annotations.Range;

import com.querydsl.core.FetchableQuery;
import com.querydsl.core.QueryModifiers;
import com.querydsl.core.ResultTransformer;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.ParamExpression;
import com.querydsl.core.types.Predicate;

public class DummyFetchableQuery<T> extends DummyFetchable<T> implements FetchableQuery<T, DummyFetchableQuery<T>> {

    public DummyFetchableQuery(List<T> results) {
        super(results);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <U> DummyFetchableQuery<U> select(Expression<U> expr) {
        return (DummyFetchableQuery<U>) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public DummyFetchableQuery<Tuple> select(Expression<?>... exprs) {
        return (DummyFetchableQuery<Tuple>) this;
    }

    @Override
    public <T1> T1 transform(ResultTransformer<T1> transformer) {
        return transformer.transform(this);
    }

    @Override
    public DummyFetchableQuery<T> limit(@Range(from = 0, to = Integer.MAX_VALUE) long limit) {
        return this;
    }

    @Override
    public DummyFetchableQuery<T> offset(@Range(from = 0, to = Integer.MAX_VALUE) long offset) {
        return this;
    }

    @Override
    public DummyFetchableQuery<T> restrict(QueryModifiers modifiers) {
        return this;
    }

    @Override
    public DummyFetchableQuery<T> orderBy(OrderSpecifier<?>... o) {
        return this;
    }

    @Override
    public <U> DummyFetchableQuery<T> set(ParamExpression<U> param, U value) {
        return this;
    }

    @Override
    public DummyFetchableQuery<T> distinct() {
        return this;
    }

    @Override
    public DummyFetchableQuery<T> where(Predicate... o) {
        return this;
    }
}
