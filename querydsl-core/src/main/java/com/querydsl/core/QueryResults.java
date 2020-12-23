/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
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
import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

/**
 * {@code QueryResults} bundles data for paged query results
 *
 * @param <T> type of result elements
 *
 * @author tiwe
 */
public final class QueryResults<T> implements Serializable {

    private static final long serialVersionUID = -4591506147471300909L;

    private static final QueryResults<Object> EMPTY = new QueryResults<Object>(
            Collections.emptyList(), Long.MAX_VALUE, 0L, 0L);

    @SuppressWarnings("unchecked")
    public static <T> QueryResults<T> emptyResults() {
        return (QueryResults<T>) EMPTY;
    };

    private final long limit, offset, total;

    private final List<T> results;

    /**
     * Create a new {@link QueryResults} instance
     *
     * @param results paged results
     * @param limit used limit
     * @param offset used offset
     * @param total total result rows count
     */
    public QueryResults(List<T> results, @Nullable Long limit, @Nullable Long offset, long total) {
        this.limit = limit != null ? limit : Long.MAX_VALUE;
        this.offset = offset != null ? offset : 0L;
        this.total = total;
        this.results = results;
    }

    /**
     * Create a new {@link QueryResults} instance
     *
     * @param results paged results
     * @param mod limit and offset
     * @param total total result rows count
     */
    public QueryResults(List<T> results, QueryModifiers mod, long total) {
        this(results, mod.getLimit(), mod.getOffset(), total);
    }

    /**
     * Get the results in List form
     *
     * An empty list is returned for no results.
     *
     * @return results
     */
    public List<T> getResults() {
        return results;
    }

    /**
     * Get the total number of results
     *
     * @return total rows
     */
    public long getTotal() {
        return total;
    }

    /**
     * Return whether there are results in the current query window
     *
     * @return true, if no results where found
     */
    public boolean isEmpty() {
        return results.isEmpty();
    }

    /**
     * Get the limit value used for the query
     *
     * @return applied limit
     */
    public long getLimit() {
        return limit;
    }

    /**
     * Get the offset value used for the query
     *
     * @return applied offset
     */
    public long getOffset() {
        return offset;
    }

}
