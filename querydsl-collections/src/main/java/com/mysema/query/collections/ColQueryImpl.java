/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.QueryMetadata;

/**
 * ColQueryImpl is the default implementation of the ColQuery interface
 * 
 * @author tiwe
 *
 */
public class ColQueryImpl extends AbstractColQuery<ColQueryImpl> implements ColQuery, Cloneable{
    
    /**
     * Create a new ColQueryImpl instance
     */
    public ColQueryImpl() {
        super(new DefaultQueryMetadata(), QueryEngine.DEFAULT);
    }
    
    /**
     * Create a new ColQueryImpl instance
     * 
     * @param evaluatorFactory
     */
    public ColQueryImpl(QueryEngine queryEngine) {
        super(new DefaultQueryMetadata(), queryEngine);
    }
    
    /**
     * Create a new ColQueryImpl instance
     * 
     * @param metadata
     * @param evaluatorFactory
     */
    public ColQueryImpl(QueryMetadata metadata, QueryEngine queryEngine) {
        super(metadata, queryEngine);
    }

    /**
     * Clone the state of this query to a new ColQueryImpl instance
     */
    public ColQueryImpl clone(){
        return new ColQueryImpl(queryMixin.getMetadata(), getQueryEngine());
    }
    
    /**
     * @return
     */
    public QueryMetadata getMetadata() {
        return queryMixin.getMetadata();
    }

}