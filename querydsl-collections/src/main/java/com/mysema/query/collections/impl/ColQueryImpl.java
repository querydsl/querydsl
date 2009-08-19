/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections.impl;

import com.mysema.query.QueryMetadata;
import com.mysema.query.collections.eval.ColQueryTemplates;


/**
 * @author tiwe
 *
 */
public class ColQueryImpl extends AbstractColQuery<ColQueryImpl>{

    public ColQueryImpl(ColQueryTemplates patterns) {
        super(patterns);
    }

    public ColQueryImpl(QueryMetadata metadata, ColQueryTemplates patterns) {
        super(metadata, patterns);
    }

}