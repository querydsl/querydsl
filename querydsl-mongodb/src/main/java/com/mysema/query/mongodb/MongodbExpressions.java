/*
 * Copyright 2011, Mysema Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mysema.query.mongodb;

import com.mysema.query.types.ConstantImpl;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Operator;
import com.mysema.query.types.OperatorImpl;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.expr.BooleanOperation;

/**
 * Mongodb specific operations
 * 
 * @author tiwe
 *
 */
public final class MongodbExpressions {
    
    public static final Operator<Boolean> NEAR = new OperatorImpl<Boolean>("MONGODB_NEAR");
    
    public static final Operator<Boolean> ELEM_MATCH = new OperatorImpl<Boolean>("MONGODB_ELEM_MATCH");

    private MongodbExpressions() {}
    
    /**
     * Finds the closest points relative to the given location and orders the results with decreasing promimity
     *
     * @param expr
     * @param latVal latitude
     * @param longVal longitude
     * @return
     */
    public static BooleanExpression near(Expression<Double[]> expr, double latVal, double longVal) {
        return BooleanOperation.create(MongodbExpressions.NEAR, expr, new ConstantImpl<Double[]>(new Double[]{latVal, longVal}));
    }
    
}
