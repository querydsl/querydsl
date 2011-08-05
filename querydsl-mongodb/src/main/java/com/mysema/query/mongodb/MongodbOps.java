/*
 * Copyright (c) 2011 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.mongodb;

import com.mysema.query.types.Operator;
import com.mysema.query.types.OperatorImpl;

public final class MongodbOps {
    
    public static final Operator<Boolean> NEAR = new OperatorImpl<Boolean>("NEAR", Number.class, Number.class);

    private MongodbOps(){}
    
}
