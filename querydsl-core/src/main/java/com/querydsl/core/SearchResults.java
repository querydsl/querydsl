/*
 * Copyright 2011, Mysema Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.core;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;

/**
 * SearchResults bundles data for paged search results
 *
 * @author tiwe
 */
public final class SearchResults<T> implements Serializable {

    private static final long serialVersionUID = -4591506147471300909L;

    private static final SearchResults<Object> EMPTY = new SearchResults<Object>(
            ImmutableList.of(), Long.MAX_VALUE, 0l, 0l); 
    
    @SuppressWarnings("unchecked")
    public static <T> SearchResults<T> emptyResults() {
        return (SearchResults<T>)EMPTY;
    };

    private final long limit, offset, total;

    private final List<T> results;

    /**
     * Create a new SearchResults instance
     * 
     * @param results paged results
     * @param limit used limit
     * @param offset used offset
     * @param total total result rows count
     */
    public SearchResults(List<T> results, @Nullable Long limit, @Nullable Long offset, long total) {
        this.limit = limit != null ? limit : Long.MAX_VALUE;
        this.offset = offset != null ? offset : 0l;
        this.total = total;
        this.results = results;
    }

    /**
     * Create a new SearchResults instance
     * 
     * @param results paged results
     * @param mod limit and offset
     * @param total total result rows count
     */
    public SearchResults(List<T> results, QueryModifiers mod, long total) {
        this(results, mod.getLimit(), mod.getOffset(), total);
    }

    /**
     * Get the results in List form
     * 
     * An empty list is returned for no results.
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
