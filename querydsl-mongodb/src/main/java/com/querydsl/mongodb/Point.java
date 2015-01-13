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
package com.querydsl.mongodb;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.expr.BooleanExpression;
import com.querydsl.core.types.path.ArrayPath;

/**
 * Point is an adapter type for Double[] arrays to use geo spatial querying features of Mongodb
 *
 * @author tiwe
 *
 */
public class Point extends ArrayPath<Double[], Double> {

    private static final long serialVersionUID = 1776628530121566388L;

    public Point(String variable) {
        super(Double[].class, variable);
    }

    public Point(Path<?> parent, String property) {
        super(Double[].class, parent, property);
    }

    public Point(PathMetadata<?> metadata) {
        super(Double[].class, metadata);
    }

    /**
     * Finds the closest points relative to the given location and orders the results with decreasing promimity
     *
     * @param latVal latitude
     * @param longVal longitude
     * @return
     */
    public BooleanExpression near(double latVal, double longVal) {
        return MongodbExpressions.near(this, latVal, longVal);
    }

}
