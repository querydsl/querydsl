/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

/**
 * QueryModifiers combines limit and offset info into a single type
 * 
 * @author Timo Westkamper
 * @version $Id$
 */
public final class QueryModifiers {

    public static QueryModifiers limit(long limit) {
        return new QueryModifiers(Long.valueOf(limit), null);
    }

    public static QueryModifiers offset(long offset) {
        return new QueryModifiers(null, Long.valueOf(offset));
    }

    private Long limit, offset;

    public QueryModifiers() {
    }

    public QueryModifiers(Long limit, Long offset) {
        this.limit = limit;
        this.offset = offset;
    }

    public Long getLimit() {
        return limit;
    }

    public Long getOffset() {
        return offset;
    }

    public void setLimit(Long limit) {
        this.limit = limit;
    }

    public void setOffset(Long offset) {
        this.offset = offset;
    }

    public boolean isRestricting() {
        return limit != null || offset != null;
    }

}
