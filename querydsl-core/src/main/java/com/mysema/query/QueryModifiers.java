/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

/**
 * QueryModifiers combines limit and offset info into a single type
 * 
 * @author Timo Westkamper
 * @version $Id$
 */
@Immutable
public final class QueryModifiers {

    public static QueryModifiers limit(long limit) {
        return new QueryModifiers(Long.valueOf(limit), null);
    }

    public static QueryModifiers offset(long offset) {
        return new QueryModifiers(null, Long.valueOf(offset));
    }

    @Nullable
    private final Long limit, offset;

    public QueryModifiers() {
        limit = null;
        offset = null;
    }

    public QueryModifiers(@Nullable Long limit, @Nullable Long offset) {
        this.limit = limit;
        this.offset = offset;
    }

    public Long getLimit() {
        return limit;
    }

    public Long getOffset() {
        return offset;
    }

    public boolean isRestricting() {
        return limit != null || offset != null;
    }

}
