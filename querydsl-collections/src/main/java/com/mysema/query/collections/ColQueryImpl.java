/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import com.mysema.query.QueryMetadata;


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