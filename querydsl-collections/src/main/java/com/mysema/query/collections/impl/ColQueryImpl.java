/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections.impl;

import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.QueryMetadata;
import com.mysema.query.collections.ColQuery;
import com.mysema.query.collections.ColQueryTemplates;


/**
 * ColQueryImpl is the default implementation of the ColQuery interface
 * 
 * @author tiwe
 *
 */
public class ColQueryImpl extends AbstractColQuery<ColQueryImpl> implements ColQuery{

    @Deprecated
    public ColQueryImpl(ColQueryTemplates templates) {
        super(new DefaultQueryMetadata(), new EvaluatorFactory(templates));
    }

    @Deprecated
    public ColQueryImpl(QueryMetadata metadata, ColQueryTemplates templates) {
        super(metadata, new EvaluatorFactory(templates));
    }
    
    public ColQueryImpl(EvaluatorFactory evaluatorFactory) {
        super(new DefaultQueryMetadata(), evaluatorFactory);
    }
    
    public ColQueryImpl(QueryMetadata metadata, EvaluatorFactory evaluatorFactory) {
        super(metadata, evaluatorFactory);
    }

}