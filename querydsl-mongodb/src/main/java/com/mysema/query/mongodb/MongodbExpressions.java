/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
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
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.expr.BooleanOperation;

/**
 * Mongodb specific operations
 *
 * @author tiwe
 *
 */
public final class MongodbExpressions {

    private MongodbExpressions() {}

    /**
     * Finds the closest points relative to the given location and orders the results with decreasing proximity
     *
     * @param expr
     * @param latVal latitude
     * @param longVal longitude
     * @return
     */
    public static BooleanExpression near(Expression<Double[]> expr, double latVal, double longVal) {
        return BooleanOperation.create(MongodbOps.NEAR, expr, ConstantImpl.create(new Double[]{latVal, longVal}));
    }

}
