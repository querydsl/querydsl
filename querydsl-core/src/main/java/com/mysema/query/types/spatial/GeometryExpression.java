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
import com.mysema.query.types.expr.SimpleOperation;
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

    private volatile SimpleExpression<byte[]> binary;

    public GeometryExpression(Expression<T> mixin) {
        super(mixin);
    }

    public NumberExpression<Integer> dimension() {
        if (dimension == null) {
            dimension = NumberOperation.create(Integer.class, Ops.SpatialOps.DIMENSION, mixin);
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
            geometryType = StringOperation.create(Ops.SpatialOps.GEOMETRY_TYPE, mixin);
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
            envelope = GeometryOperation.create(null, Ops.SpatialOps.ENVELOPE, mixin);
        }
        return envelope;
    }

    public StringExpression asText() {
        if (text == null) {
            text = StringOperation.create(Ops.SpatialOps.AS_TEXT, mixin);
        }
        return text;
    }

    public SimpleExpression<byte[]> asBinary() {
        if (binary == null) {
            binary = SimpleOperation.create(byte[].class, Ops.SpatialOps.AS_BINARY, mixin);
        }
        return binary;
    }

    public BooleanExpression isEmpty() {
        if (empty == null) {
            empty = BooleanOperation.create(Ops.SpatialOps.IS_EMPTY, mixin);
        }
        return empty;
    }

    public BooleanExpression isSimple() {
        if (simple == null) {
            empty = BooleanOperation.create(Ops.SpatialOps.IS_SIMPLE, mixin);
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
            boundary = GeometryOperation.create(null, Ops.SpatialOps.BOUNDARY, mixin);
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
        return BooleanOperation.create(Ops.SpatialOps.EQUALS, mixin, right);
    }

    public BooleanExpression disjoint(T geometry) {
        return disjoint(ConstantImpl.create(geometry));
    }

    public BooleanExpression disjoint(Expression<T> geometry) {
        return BooleanOperation.create(Ops.SpatialOps.DISJOINT, mixin, geometry);
    }

    public BooleanExpression intersects(T geometry) {
        return intersects(ConstantImpl.create(geometry));
    }

    public BooleanExpression intersects(Expression<T> geometry) {
        return BooleanOperation.create(Ops.SpatialOps.INTERSECTS, mixin, geometry);
    }

    public BooleanExpression touches(T geometry) {
        return touches(ConstantImpl.create(geometry));
    }

    public BooleanExpression touches(Expression<T> geometry) {
        return BooleanOperation.create(Ops.SpatialOps.TOUCHES, mixin, geometry);
    }

    public BooleanExpression crosses(T geometry) {
         return crosses(ConstantImpl.create(geometry));
    }

    public BooleanExpression crosses(Expression<T> geometry) {
        return BooleanOperation.create(Ops.SpatialOps.CROSSES, mixin, geometry);
    }

    public BooleanExpression within(T geometry) {
        return within(ConstantImpl.create(geometry));
    }

    public BooleanExpression within(Expression<T> geometry) {
        return BooleanOperation.create(Ops.SpatialOps.WITHIN, mixin, geometry);
    }

    public BooleanExpression contains(T geometry) {
        return contains(ConstantImpl.create(geometry));
    }

    public BooleanExpression contains(Expression<T> geometry) {
        return BooleanOperation.create(Ops.SpatialOps.CONTAINS, mixin, geometry);
    }

    public BooleanExpression overlaps(T geometry) {
        return overlaps(ConstantImpl.create(geometry));
    }

    public BooleanExpression overlaps(Expression<T> geometry) {
        return BooleanOperation.create(Ops.SpatialOps.OVERLAPS, mixin, geometry);
    }

    public BooleanExpression relate(T geometry, String matrix) {
        return BooleanOperation.create(Ops.SpatialOps.RELATE, mixin,
                ConstantImpl.create(geometry), ConstantImpl.create(matrix));
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

    public NumberExpression<Double> distance(T geometry) {
        return distance(ConstantImpl.create(geometry));
    }

    public NumberExpression<Double> distance(Expression<T> geometry) {
        return NumberOperation.create(Double.class, Ops.SpatialOps.DISTANCE, mixin, geometry);
    }

    public GeometryExpression<T> buffer(double distance) {
        return GeometryOperation.create(null, Ops.SpatialOps.BUFFER, mixin, ConstantImpl.create(distance));
    }

    public GeometryExpression<T> convexHull() {
        if (convexHull == null) {
            convexHull = GeometryOperation.create(null, Ops.SpatialOps.CONVEXHULL, mixin);
        }
        return convexHull;
    }

    public GeometryExpression<T> intersection(T geometry) {
        return intersection(ConstantImpl.create(geometry));
    }

    public GeometryExpression<T> intersection(Expression<T> geometry) {
        return GeometryOperation.create(null, Ops.SpatialOps.INTERSECTION, mixin, geometry);
    }

    public GeometryExpression<T> union(T geometry) {
        return union(ConstantImpl.create(geometry));
    }

    public GeometryExpression<T> union(Expression<T> geometry) {
        return GeometryOperation.create(null, Ops.SpatialOps.UNION, mixin, geometry);
    }

    public GeometryExpression<T> difference(T geometry) {
        return difference(ConstantImpl.create(geometry));
    }

    public GeometryExpression<T> difference(Expression<T> geometry) {
        return GeometryOperation.create(null, Ops.SpatialOps.UNION, mixin, geometry);
    }

    public GeometryExpression<T> symDifference(T geometry) {
        return symDifference(ConstantImpl.create(geometry));
    }

    public GeometryExpression<T> symDifference(Expression<T> geometry) {
        return GeometryOperation.create(null, Ops.SpatialOps.SYMDIFFERENCE, mixin, geometry);
    }

}
