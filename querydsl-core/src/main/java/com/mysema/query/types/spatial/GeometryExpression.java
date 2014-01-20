/*
 * Copyright 2014, Mysema Ltd
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
package com.mysema.query.types.spatial;

import javax.annotation.Nullable;

import com.mysema.query.types.ConstantImpl;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Ops;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.expr.BooleanOperation;
import com.mysema.query.types.expr.NumberExpression;
import com.mysema.query.types.expr.NumberOperation;
import com.mysema.query.types.expr.SimpleExpression;
import com.mysema.query.types.expr.StringExpression;
import com.mysema.query.types.expr.StringOperation;

public abstract class GeometryExpression<T> extends SimpleExpression<T> {

    private static final long serialVersionUID = -1183228394472681995L;

    @Nullable
    private volatile NumberExpression<Integer> dimension, coordinateDimension, spatialDimension, srid;

    @Nullable
    private volatile StringExpression geometryType;

    @Nullable
    private volatile StringExpression text;

    @Nullable
    private volatile GeometryExpression<T> envelope, boundary, convexHull;

    @Nullable
    private volatile BooleanExpression empty, simple, threed, measured;

    public GeometryExpression(Expression<T> mixin) {
        super(mixin);
    }

    public NumberExpression<Integer> dimension() {
        if (dimension == null) {
            dimension = NumberOperation.create(Integer.class, Ops.SpatialOps.Dimension, mixin);
        }
        return dimension;
    }

    public NumberExpression<Integer> coordinateDimension() {
        if (coordinateDimension == null) {
            // TODO
        }
        return coordinateDimension;
    }

    public NumberExpression<Integer> spatialDimension() {
        if (spatialDimension == null) {
            // TODO
        }
        return spatialDimension;
    }

    public StringExpression geometryType() {
        if (geometryType == null) {
            geometryType = StringOperation.create(Ops.SpatialOps.GeometryType, mixin);
        }
        return geometryType;
    }

    public NumberExpression<Integer> SRID() {
        if (srid == null) {
            srid = NumberOperation.create(Integer.class, Ops.SpatialOps.SRID, mixin);
        }
        return srid;
    }

    public GeometryExpression<T> envelope() {
        if (envelope == null) {
            envelope = GeometryOperation.create(null, Ops.SpatialOps.Envelope, mixin);
        }
        return envelope;
    }

    public StringExpression asText() {
        if (text == null) {
            text = StringOperation.create(Ops.SpatialOps.AsText, mixin);
        }
        return text;
    }

    public SimpleExpression<Object> asBinary() {
        // TODO change type of expression
        return null;
    }

    public BooleanExpression isEmpty() {
        if (empty == null) {
            empty = BooleanOperation.create(Ops.SpatialOps.IsEmpty, mixin);
        }
        return empty;
    }

    public BooleanExpression isSimple() {
        if (simple == null) {
            empty = BooleanOperation.create(Ops.SpatialOps.IsSimple, mixin);
        }
        return simple;
    }

    public BooleanExpression is3D() {
        if (threed == null) {
            // TODO
        }
        return threed;
    }

    public BooleanExpression isMeasured() {
        if (measured == null) {
            // TODO
        }
        return measured;
    }

    public GeometryExpression<T> boundary() {
        if (boundary == null) {
            boundary = GeometryOperation.create(null, Ops.SpatialOps.Boundary, mixin);
        }
        return boundary;
    }

    // query

    @Override
    public BooleanExpression eq(T right) {
        return eq(ConstantImpl.create(right));
    }

    @Override
    public BooleanExpression eq(Expression<? super T> right) {
        return BooleanOperation.create(Ops.SpatialOps.Equals, mixin, right);
    }

    public BooleanExpression disjoint(T geometry) {
        return disjoint(ConstantImpl.create(geometry));
    }

    public BooleanExpression disjoint(Expression<T> geometry) {
        return BooleanOperation.create(Ops.SpatialOps.Disjoint, mixin, geometry);
    }

    public BooleanExpression intersects(T geometry) {
        return intersects(ConstantImpl.create(geometry));
    }

    public BooleanExpression intersects(Expression<T> geometry) {
        return BooleanOperation.create(Ops.SpatialOps.Intersects, mixin, geometry);
    }

    public BooleanExpression touches(T geometry) {
        return touches(ConstantImpl.create(geometry));
    }

    public BooleanExpression touches(Expression<T> geometry) {
        return BooleanOperation.create(Ops.SpatialOps.Touches, mixin, geometry);
    }

    public BooleanExpression crosses(T geometry) {
         return crosses(ConstantImpl.create(geometry));
    }

    public BooleanExpression crosses(Expression<T> geometry) {
        return BooleanOperation.create(Ops.SpatialOps.Crosses, mixin, geometry);
    }

    public BooleanExpression within(T geometry) {
        return within(ConstantImpl.create(geometry));
    }

    public BooleanExpression within(Expression<T> geometry) {
        return BooleanOperation.create(Ops.SpatialOps.Within, mixin, geometry);
    }

    public BooleanExpression contains(T geometry) {
        return contains(ConstantImpl.create(geometry));
    }

    public BooleanExpression contains(Expression<T> geometry) {
        return BooleanOperation.create(Ops.SpatialOps.Contains, mixin, geometry);
    }

    public BooleanExpression overlaps(T geometry) {
        return overlaps(ConstantImpl.create(geometry));
    }

    public BooleanExpression overlaps(Expression<T> geometry) {
        return BooleanOperation.create(Ops.SpatialOps.Overlaps, mixin, geometry);
    }

    public BooleanExpression relate(T geometry, String matrix) {
        return null;
    }

    public GeometryExpression<T> locateAlong(double value) {
        return locateAlong(ConstantImpl.create(value));
    }

    public GeometryExpression<T> locateAlong(Expression<Double> value) {
        return null;
    }

    public GeometryExpression<T> locateBetween(double start, double end) {
        return null;
    }

    // analysis

    public Object distance(T geometry) { // TODO Distance type
        return null;
    }

    public GeometryExpression<T> buffer(Object distance) {
        return null;
    }

    public GeometryExpression<T> convexHull() {
        if (convexHull == null) {
            convexHull = GeometryOperation.create(null, Ops.SpatialOps.ConvexHull, mixin);
        }
        return convexHull;
    }

    public GeometryExpression<T> intersection(T geometry) {
        return null;
    }

    public GeometryExpression<T> union(T geometry) {
        return null;
    }

    public GeometryExpression<T> difference(T geometry) {
        return null;
    }

    public GeometryExpression<T> symDifference(T geometry) {
        return null;
    }

}
