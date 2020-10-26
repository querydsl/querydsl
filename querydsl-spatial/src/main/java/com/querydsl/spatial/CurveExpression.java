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
package com.querydsl.spatial;

import org.jetbrains.annotations.Nullable;

import org.geolatte.geom.Geometry;
import org.geolatte.geom.Point;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;

/**
 * A Curve is a 1-dimensional geometric object usually stored as a sequence of Points, with the subtype of Curve
 * specifying the form of the interpolation between Points. This standard defines only one subclass of Curve,
 * LineString, which uses linear interpolation between Points.
 *
 * @author tiwe
 *
 * @param <T>
 */
public abstract class CurveExpression<T extends Geometry> extends GeometryExpression<T> {

    private static final long serialVersionUID = 6139188586728676033L;

    @Nullable
    private transient volatile NumberExpression<Double> length;

    @Nullable
    private transient volatile PointExpression<Point> startPoint, endPoint;

    @Nullable
    private transient volatile BooleanExpression closed, ring;

    public CurveExpression(Expression<T> mixin) {
        super(mixin);
    }

    /**
     * The length of this Curve in its associated spatial reference.
     *
     * @return length
     */
    public NumberExpression<Double> length() {
        if (length == null) {
            length = Expressions.numberOperation(Double.class, SpatialOps.LENGTH, mixin);
        }
        return length;
    }

    /**
     * The start Point of this Curve.
     *
     * @return start point
     */
    public PointExpression<Point> startPoint() {
        if (startPoint == null) {
            startPoint = GeometryExpressions.pointOperation(SpatialOps.START_POINT, mixin);
        }
        return startPoint;
    }

    /**
     * The end Point of this Curve.
     *
     * @return end point
     */
    public PointExpression<Point> endPoint() {
        if (endPoint == null) {
            endPoint = GeometryExpressions.pointOperation(SpatialOps.END_POINT, mixin);
        }
        return endPoint;
    }

    /**
     * Returns 1 (TRUE) if this Curve is closed [StartPoint ( ) = EndPoint ( )].
     *
     * @return closed
     */
    public BooleanExpression isClosed() {
        if (closed == null) {
            closed = Expressions.booleanOperation(SpatialOps.IS_CLOSED, mixin);
        }
        return closed;
    }

    /**
     * Returns 1 (TRUE) if this Curve is closed [StartPoint ( ) = EndPoint ( )] and this Curve is
     * simple (does not pass through the same Point more than once).
     *
     * @return ring
     */
    public BooleanExpression isRing() {
        if (ring == null) {
            ring = Expressions.booleanOperation(SpatialOps.IS_RING, mixin);
        }
        return ring;
    }

}
