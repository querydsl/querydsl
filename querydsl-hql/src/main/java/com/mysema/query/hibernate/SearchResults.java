/*
 * Copyright (c) 2007 Mysema Ltd.
 * All rights reserved.
 * 
 * originally developed in Bookmarks project
 */
package com.mysema.query.hibernate;

import java.util.Collections;
import java.util.List;

/**
 * SearchResults provides.
 * 
 * @author Timo Westkamper
 * @version $Id$
 */
public final class SearchResults<T> {
    
    public static <T> SearchResults<T> emptyResults(){
        return new SearchResults<T>(Collections.<T>emptyList(), Long.MAX_VALUE, 0l,0l);
    };

    private final long limit, offset, total;
    
    private final List<T> results;
    
    public SearchResults(List<T> results, long limit, long offset, long total){
        this.limit = limit;
        this.offset = offset;
        this.total = total;
        this.results = results;
    }
    
    public SearchResults(List<T> results, QueryModifiers mod, long total){
        this.limit = mod.getLimit();
        this.offset = mod.getOffset();
        this.total = results.size();
        this.results = results;
    }
    
    public List<T> getResults() {
        return results;
    }

    public long getTotal() {
        return total;
    }

    public boolean isEmpty() {
        return results.isEmpty();
    }

    public long getLimit() {
        return limit;
    }

    public long getOffset() {
        return offset;
    }
    
}
