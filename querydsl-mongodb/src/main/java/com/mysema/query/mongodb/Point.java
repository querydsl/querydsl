/*
 * Copyright (c) 2011 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.mongodb;

import com.mysema.query.types.ConstantImpl;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.expr.BooleanOperation;
import com.mysema.query.types.path.ArrayPath;

/**
 * Point is an adapter type for Double[] arrays to use geo spatial querying features of Mongodb
 *
 * @author tiwe
 *
 */
public class Point extends ArrayPath<Double> {

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
    public BooleanExpression near(double latVal, double longVal){
        return BooleanOperation.create(MongodbOps.NEAR, this, new ConstantImpl<Double[]>(new Double[]{latVal, longVal}));
    }

}
