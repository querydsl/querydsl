/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import javax.annotation.Nullable;

import net.jcip.annotations.Immutable;

// TODO: Auto-generated Javadoc
/**
 * QueryModifiers combines limit and offset info into a single type.
 * 
 * @author Timo Westkamper
 * @version $Id$
 */
@Immutable
public final class QueryModifiers {

    /**
     * Limit.
     * 
     * @param limit the limit
     * 
     * @return the query modifiers
     */
    public static QueryModifiers limit(long limit) {
        return new QueryModifiers(Long.valueOf(limit), null);
    }

    /**
     * Offset.
     * 
     * @param offset the offset
     * 
     * @return the query modifiers
     */
    public static QueryModifiers offset(long offset) {
        return new QueryModifiers(null, Long.valueOf(offset));
    }

    /** The offset. */
    @Nullable
    private final Long limit, offset;

    /**
     * Instantiates a new query modifiers.
     */
    public QueryModifiers() {
        limit = null;
        offset = null;
    }

    /**
     * Instantiates a new query modifiers.
     * 
     * @param limit the limit
     * @param offset the offset
     */
    public QueryModifiers(@Nullable Long limit, @Nullable Long offset) {
        this.limit = limit;
        this.offset = offset;
    }

    /**
     * Gets the limit.
     * 
     * @return the limit
     */
    public Long getLimit() {
        return limit;
    }

    /**
     * Gets the offset.
     * 
     * @return the offset
     */
    public Long getOffset() {
        return offset;
    }

    /**
     * Checks if is restricting.
     * 
     * @return true, if is restricting
     */
    public boolean isRestricting() {
        return limit != null || offset != null;
    }

}
