/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types;

import javax.annotation.Nullable;

/**
 * Default implementation of the interval interface
 *
 * @author tiwe
 *
 * @param <T>
 */
public class IntervalImpl<T> implements Interval<T> {

    public static <T> Interval<T> create(@Nullable T begin, @Nullable T end){
        return new IntervalImpl<T>(begin, end);
    }

    @Nullable
    private final T begin, end;

    public IntervalImpl(@Nullable T begin, @Nullable T end){
        this.begin = begin;
        this.end = end;
    }

    @Override
    public T getBegin() {
        return begin;
    }

    @Override
    public T getEnd() {
        return end;
    }

}
