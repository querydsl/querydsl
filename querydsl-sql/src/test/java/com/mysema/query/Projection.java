/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query;

import javax.annotation.Nullable;

import com.mysema.query.types.Expression;

public interface Projection {

    @Nullable
    <T> T get(Expression<T> expr);

    @Nullable
    <T> T get(int index, Class<T> type);

    @Nullable
    <T> Expression<T> getExpr(Expression<T> expr);

    @Nullable
    <T> Expression<T> getExpr(int index, Class<T> type);

    Object[] toArray();

}
