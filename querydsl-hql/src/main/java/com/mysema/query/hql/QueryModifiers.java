/*
 * Copyright (c) 2007 Mysema Ltd.
 * All rights reserved.
 * 
 * originally developed in Bookmarks project
 */
package com.mysema.query.hql;

/**
 * QueryModifiers combines limit and offset info into a single type
 * 
 * @author Timo Westkamper
 * @version $Id$
 */
// TODO : move these to querydsl-core
public final class QueryModifiers {
    
    private static final QueryModifiers DEFAULT = new QueryModifiers(Integer.MAX_VALUE,0);
    
    private final int limit, offset;
    
    public QueryModifiers(int limit, int offset){
        this.limit = limit;
        this.offset = offset;
    }

    public static QueryModifiers getDefault(){
        return DEFAULT;
    }

    public int getLimit() {
        return limit;
    }
    
    public int getOffset() {
        return offset;
    }
    
}
