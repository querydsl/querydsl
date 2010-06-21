/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types;

import javax.annotation.Nullable;

/**
 * Interval is a typed inclusive interval from begin to end with optional open begin and end
 *
 * @author tiwe
 *
 * @param <T>
 */
public interface Interval<T> {

    /**
     * Get the beginning of the interval or null
     *
     * @return
     */
    @Nullable
    T getBegin();

    /**
     * Get the end of the interval or null
     *
     * @return
     */
    @Nullable
    T getEnd();

}
