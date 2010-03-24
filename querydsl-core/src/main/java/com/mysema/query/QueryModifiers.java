/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query;

import java.io.Serializable;

import javax.annotation.Nonnegative;
import javax.annotation.Nullable;

import net.jcip.annotations.Immutable;

/**
 * QueryModifiers combines limit and offset info into a single type.
 *
 * @author Timo Westkamper
 * @version $Id$
 */
@Immutable
public final class QueryModifiers implements Serializable{

    private static final long serialVersionUID = 2934344588433680339L;

    /**
     * Limit.
     *
     * @param limit the limit
     *
     * @return the query modifiers
     */
    public static QueryModifiers limit(@Nonnegative long limit) {
        return new QueryModifiers(Long.valueOf(limit), null);
    }

    /**
     * Offset.
     *
     * @param offset the offset
     *
     * @return the query modifiers
     */
    public static QueryModifiers offset(@Nonnegative long offset) {
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
        if (limit != null && limit <= 0) {
            throw new IllegalArgumentException("Limit must be greater than 0.");
        }
        this.offset = offset;
        if (offset != null && offset < 0) {
            throw new IllegalArgumentException("Limit must not be negative.");
        }
    }

    /**
     * @param modifiers
     */
    public QueryModifiers(QueryModifiers modifiers) {
        this.limit = modifiers.getLimit();
        this.offset = modifiers.getOffset();
    }

    /**
     * Gets the limit.
     *
     * @return the limit
     */
    @Nullable
    public Long getLimit() {
        return limit;
    }

    /**
     * Gets the offset.
     *
     * @return the offset
     */
    @Nullable
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
