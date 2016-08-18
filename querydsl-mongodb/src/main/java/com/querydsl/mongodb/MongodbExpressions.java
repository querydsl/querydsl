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
package com.querydsl.mongodb;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import org.mongodb.morphia.query.Shape;

/**
 * Mongodb specific operations
 *
 * @author tiwe
 *
 */
public final class MongodbExpressions {

    private MongodbExpressions() { }

    /**
     * Finds the closest points relative to the given location and orders the results with decreasing proximity
     *
     * @param expr location
     * @param latVal latitude
     * @param longVal longitude
     * @return predicate
     */
    public static BooleanExpression near(Expression<Double[]> expr, double latVal, double longVal) {
        return Expressions.booleanOperation(MongodbOps.NEAR, expr, Expressions.constant(new Double[]{latVal, longVal}));
    }

    /**
     * Finds the closest points relative to the given location on a sphere and orders the results with decreasing proximity
     *
     * @param expr location
     * @param latVal latitude
     * @param longVal longitude
     * @return predicate
     */
    public static BooleanExpression nearSphere(Expression<Double[]> expr, double latVal, double longVal) {
        return Expressions.booleanOperation(MongodbOps.NEAR_SPHERE, expr, Expressions.constant(new Double[]{latVal, longVal}));
    }

    /**
     * Finds the all the points within the given shape
     *
     * @param expr expression
     * @param shape shape
     * @return predicate
     */
    public static BooleanExpression geoWithin(Expression<Double[]> expr, Shape shape) {
        return Expressions.booleanOperation(MongodbOps.GEO_WITHIN, expr, Expressions.constant(shape));
    }
}
