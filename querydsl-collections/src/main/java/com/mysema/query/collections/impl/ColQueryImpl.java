/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections.impl;

import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.QueryMetadata;
import com.mysema.query.collections.ColQuery;


/**
 * ColQueryImpl is the default implementation of the ColQuery interface
 * 
 * @author tiwe
 *
 */
public class ColQueryImpl extends AbstractColQuery<ColQueryImpl> implements ColQuery{
    
    public ColQueryImpl() {
        super(new DefaultQueryMetadata(), EvaluatorFactory.DEFAULT);
    }
    
    public ColQueryImpl(EvaluatorFactory evaluatorFactory) {
        super(new DefaultQueryMetadata(), evaluatorFactory);
    }
    
    public ColQueryImpl(QueryMetadata metadata, EvaluatorFactory evaluatorFactory) {
        super(metadata, evaluatorFactory);
    }

    public QueryMetadata getMetadata() {
        return queryMixin.getMetadata();
    }

    /**
     * Clone the state of this query to a new ColQueryImpl instance
     */
    public ColQueryImpl clone(){
        return new ColQueryImpl(queryMixin.getMetadata(), getEvaluatorFactory());
    }

}