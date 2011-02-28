package com.mysema.query.mongodb;

import com.mysema.query.types.Operator;
import com.mysema.query.types.OperatorImpl;

public final class MongodbOps {
    
    public static Operator<Boolean> NEAR = new OperatorImpl<Boolean>("NEAR", Number.class, Number.class);

    private MongodbOps(){}
    
}
