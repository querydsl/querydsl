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
package com.querydsl.mongodb.morphia;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.mongodb.MongodbOps;
import org.mongodb.morphia.geo.Point;

/**
 * Morphia specific MongoDB operations
 *
 * @author jmoghisi
 *
 */
public final class MorphiaExpressions {

    private MorphiaExpressions() { }

    /**
     * Finds the closest points relative to the given location and orders the results with decreasing proximity
     *
     * @param expr expression
     * @param point location
     * @return predicate
     */
    public static BooleanExpression near(Expression<Point> expr, Point point) {
        return Expressions.booleanOperation(MongodbOps.NEAR, expr, Expressions.constant(point));
    }

    /**
     * Finds the closest points relative to the given location on a sphere and orders the results with decreasing proximity
     *
     * @param expr expression
     * @param point location
     * @return predicate
     */
    public static BooleanExpression nearSphere(Expression<Point> expr, Point point) {
        return Expressions.booleanOperation(MongodbOps.NEAR_SPHERE, expr, Expressions.constant(point));
    }

}
