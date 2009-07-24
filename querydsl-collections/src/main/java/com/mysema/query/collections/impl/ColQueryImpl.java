/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections.impl;

import com.mysema.query.QueryMetadata;
import com.mysema.query.collections.eval.ColQueryPatterns;


/**
 * @author tiwe
 *
 */
public class ColQueryImpl extends AbstractColQuery<ColQueryImpl>{

    public ColQueryImpl() {
        super();
    }

    public ColQueryImpl(ColQueryPatterns patterns) {
        super(patterns);
    }

    public ColQueryImpl(QueryMetadata metadata) {
        super(metadata);
    }

}