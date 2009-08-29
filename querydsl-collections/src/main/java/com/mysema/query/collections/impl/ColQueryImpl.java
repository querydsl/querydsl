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
 * @author tiwe
 *
 */
public class ColQueryImpl extends AbstractColQuery<ColQueryImpl> implements ColQuery{

    public ColQueryImpl(ColQueryTemplates patterns) {
        super(new DefaultQueryMetadata(), patterns);
    }

    public ColQueryImpl(QueryMetadata metadata, ColQueryTemplates patterns) {
        super(metadata, patterns);
    }

}