/*
 * Copyright 2014, Timo Westkämper
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
package com.querydsl.spatial.jts;

import javax.annotation.Nullable;

import com.querydsl.spatial.SpatialOps;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.expr.BooleanExpression;
import com.querydsl.core.types.expr.BooleanOperation;
import com.querydsl.core.types.expr.NumberExpression;
import com.querydsl.core.types.expr.NumberOperation;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

/**
 * A Curve is a 1-dimensional geometric object usually stored as a sequence of Points, with the subtype of Curve
 * specifying the form of the interpolation between Points. This standard defines only one subclass of Curve,
 * LineString, which uses linear interpolation between Points.
 *
 * @author tiwe
 *
 * @param <T>
 */
public abstract class JTSCurveExpression<T extends Geometry> extends JTSGeometryExpression<T> {

    private static final long serialVersionUID = 6139188586728676033L;

    @Nullable
    private volatile NumberExpression<Double> length;

    @Nullable
    private volatile JTSPointExpression<Point> startPoint, endPoint;

    @Nullable
    private volatile BooleanExpression closed, ring;

    public JTSCurveExpression(Expression<T> mixin) {
        super(mixin);
    }

    /**
     * The length of this Curve in its associated spatial reference.
     *
     * @return
     */
    public NumberExpression<Double> length() {
        if (length == null) {
            length = NumberOperation.create(Double.class, SpatialOps.LENGTH, mixin);
        }
        return length;
    }

    /**
     * The start Point of this Curve.
     *
     * @return
     */
    public JTSPointExpression<Point> startPoint() {
        if (startPoint == null) {
            startPoint = JTSPointOperation.create(Point.class, SpatialOps.START_POINT, mixin);
        }
        return startPoint;
    }

    /**
     * The end Point of this Curve.
     *
     * @return
     */
    public JTSPointExpression<Point> endPoint() {
        if (endPoint == null) {
            endPoint = JTSPointOperation.create(Point.class, SpatialOps.END_POINT, mixin);
        }
        return endPoint;
    }

    /**
     * Returns 1 (TRUE) if this Curve is closed [StartPoint ( ) = EndPoint ( )].
     *
     * @return
     */
    public BooleanExpression isClosed() {
        if (closed == null) {
            closed = BooleanOperation.create(SpatialOps.IS_CLOSED, mixin);
        }
        return closed;
    }

    /**
     * Returns 1 (TRUE) if this Curve is closed [StartPoint ( ) = EndPoint ( )] and this Curve is
     * simple (does not pass through the same Point more than once).
     *
     * @return
     */
    public BooleanExpression isRing() {
        if (ring == null) {
            ring = BooleanOperation.create(SpatialOps.IS_RING, mixin);
        }
        return ring;
    }

}
