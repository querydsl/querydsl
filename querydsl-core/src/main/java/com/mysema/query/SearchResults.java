/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import net.jcip.annotations.Immutable;

/**
 * SearchResults bundles data for paged search results
 * 
 * @author tiwe
 */
@Immutable
public final class SearchResults<T> {

    public static <T> SearchResults<T> emptyResults() {
        return new SearchResults<T>(Collections.<T> emptyList(),
                Long.MAX_VALUE, 0l, 0l);
    };

    private final long limit, offset, total;

    private final List<T> results;

    public SearchResults(List<T> results, @Nullable Long limit, @Nullable Long offset, long total) {
        this.limit = limit != null ? limit : Long.MAX_VALUE;
        this.offset = offset != null ? offset : 0l;
        this.total = total;
        this.results = results;
    }

    public SearchResults(List<T> results, QueryModifiers mod, long total) {
        this(results, mod.getLimit(), mod.getOffset(), total);
    }

    /**
     * Get the results in List form
     * 
     * @return
     */
    public List<T> getResults() {
        return results;
    }

    /**
     * Get the number of total results
     * 
     * @return
     */
    public long getTotal() {
        return total;
    }

    /**
     * @return
     */
    public boolean isEmpty() {
        return results.isEmpty();
    }

    /**
     * @return
     */
    public long getLimit() {
        return limit;
    }

    /**
     * @return
     */
    public long getOffset() {
        return offset;
    }

}
