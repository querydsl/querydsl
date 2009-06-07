/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import com.mysema.query.QueryMetadata;


public class ColQueryImpl extends AbstractColQuery<ColQueryImpl>{

    public ColQueryImpl() {
        super();
    }

    public ColQueryImpl(ColQueryPatterns patterns) {
        super(patterns);
    }

    public ColQueryImpl(QueryMetadata<Object> metadata) {
        super(metadata);
    }

}