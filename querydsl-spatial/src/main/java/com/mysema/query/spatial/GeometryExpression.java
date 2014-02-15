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
package com.mysema.query.spatial;

import javax.annotation.Nullable;

import org.geolatte.geom.Geometry;

import com.mysema.query.types.ConstantImpl;
import com.mysema.query.types.Expression;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.expr.BooleanOperation;
import com.mysema.query.types.expr.NumberExpression;
import com.mysema.query.types.expr.NumberOperation;
import com.mysema.query.types.expr.SimpleExpression;
import com.mysema.query.types.expr.SimpleOperation;
import com.mysema.query.types.expr.StringExpression;
import com.mysema.query.types.expr.StringOperation;

/**
 * @author tiwe
 *
 * @param <T>
 */
public abstract class GeometryExpression<T extends Geometry> extends SimpleExpression<T> {

    private static final long serialVersionUID = -1183228394472681995L;

    @Nullable
    private volatile NumberExpression<Integer> dimension, coordinateDimension, spatialDimension, srid;

    @Nullable
    private volatile StringExpression geometryType;

    @Nullable
    private volatile StringExpression text;

    @Nullable
    private volatile GeometryExpression<Geometry> envelope, boundary, convexHull;

    @Nullable
    private volatile BooleanExpression empty, simple, threed, measured;

    private volatile SimpleExpression<byte[]> binary;

    public GeometryExpression(Expression<T> mixin) {
        super(mixin);
    }

    public NumberExpression<Integer> dimension() {
        if (dimension == null) {
            dimension = NumberOperation.create(Integer.class, SpatialOps.DIMENSION, mixin);
        }
        return dimension;
    }

    public StringExpression geometryType() {
        if (geometryType == null) {
            geometryType = StringOperation.create(SpatialOps.GEOMETRY_TYPE, mixin);
        }
        return geometryType;
    }

    public NumberExpression<Integer> srid() {
        if (srid == null) {
            srid = NumberOperation.create(Integer.class, SpatialOps.SRID, mixin);
        }
        return srid;
    }

    public GeometryExpression<Geometry> envelope() {
        if (envelope == null) {
            envelope = GeometryOperation.create(Geometry.class, SpatialOps.ENVELOPE, mixin);
        }
        return envelope;
    }

    public StringExpression asText() {
        if (text == null) {
            text = StringOperation.create(SpatialOps.AS_TEXT, mixin);
        }
        return text;
    }

    public SimpleExpression<byte[]> asBinary() {
        if (binary == null) {
            binary = SimpleOperation.create(byte[].class, SpatialOps.AS_BINARY, mixin);
        }
        return binary;
    }

    public BooleanExpression isEmpty() {
        if (empty == null) {
            empty = BooleanOperation.create(SpatialOps.IS_EMPTY, mixin);
        }
        return empty;
    }

    public BooleanExpression isSimple() {
        if (simple == null) {
            simple = BooleanOperation.create(SpatialOps.IS_SIMPLE, mixin);
        }
        return simple;
    }

    public GeometryExpression<Geometry> boundary() {
        if (boundary == null) {
            boundary = GeometryOperation.create(Geometry.class, SpatialOps.BOUNDARY, mixin);
        }
        return boundary;
    }

    // query

    @Override
    public BooleanExpression eq(Geometry right) {
        return eq(ConstantImpl.create(right));
    }

    @Override
    public BooleanExpression eq(Expression<? super T> right) {
        return BooleanOperation.create(SpatialOps.EQUALS, mixin, right);
    }

    public BooleanExpression disjoint(Geometry geometry) {
        return disjoint(ConstantImpl.create(geometry));
    }

    public BooleanExpression disjoint(Expression<? extends Geometry> geometry) {
        return BooleanOperation.create(SpatialOps.DISJOINT, mixin, geometry);
    }

    public BooleanExpression intersects(Geometry geometry) {
        return intersects(ConstantImpl.create(geometry));
    }

    public BooleanExpression intersects(Expression<? extends Geometry> geometry) {
        return BooleanOperation.create(SpatialOps.INTERSECTS, mixin, geometry);
    }

    public BooleanExpression touches(Geometry geometry) {
        return touches(ConstantImpl.create(geometry));
    }

    public BooleanExpression touches(Expression<? extends Geometry> geometry) {
        return BooleanOperation.create(SpatialOps.TOUCHES, mixin, geometry);
    }

    public BooleanExpression crosses(Geometry geometry) {
         return crosses(ConstantImpl.create(geometry));
    }

    public BooleanExpression crosses(Expression<? extends Geometry> geometry) {
        return BooleanOperation.create(SpatialOps.CROSSES, mixin, geometry);
    }

    public BooleanExpression within(Geometry geometry) {
        return within(ConstantImpl.create(geometry));
    }

    public BooleanExpression within(Expression<? extends Geometry> geometry) {
        return BooleanOperation.create(SpatialOps.WITHIN, mixin, geometry);
    }

    public BooleanExpression contains(Geometry geometry) {
        return contains(ConstantImpl.create(geometry));
    }

    public BooleanExpression contains(Expression<? extends Geometry> geometry) {
        return BooleanOperation.create(SpatialOps.CONTAINS, mixin, geometry);
    }

    public BooleanExpression overlaps(Geometry geometry) {
        return overlaps(ConstantImpl.create(geometry));
    }

    public BooleanExpression overlaps(Expression<? extends Geometry> geometry) {
        return BooleanOperation.create(SpatialOps.OVERLAPS, mixin, geometry);
    }

    public BooleanExpression relate(Geometry geometry, String matrix) {
        return relate(ConstantImpl.create(geometry), matrix);
    }

    public BooleanExpression relate(Expression<? extends Geometry> geometry, String matrix) {
        return BooleanOperation.create(SpatialOps.RELATE, mixin, geometry, ConstantImpl.create(matrix));
    }

    // analysis

    public NumberExpression<Double> distance(Geometry geometry) {
        return distance(ConstantImpl.create(geometry));
    }

    public NumberExpression<Double> distance(Expression<? extends Geometry> geometry) {
        return NumberOperation.create(Double.class, SpatialOps.DISTANCE, mixin, geometry);
    }

    public GeometryExpression<Geometry> buffer(double distance) {
        return GeometryOperation.create(Geometry.class, SpatialOps.BUFFER, mixin, ConstantImpl.create(distance));
    }

    public GeometryExpression<Geometry> convexHull() {
        if (convexHull == null) {
            convexHull = GeometryOperation.create(Geometry.class, SpatialOps.CONVEXHULL, mixin);
        }
        return convexHull;
    }

    public GeometryExpression<Geometry> intersection(Geometry geometry) {
        return intersection(ConstantImpl.create(geometry));
    }

    public GeometryExpression<Geometry> intersection(Expression<? extends Geometry> geometry) {
        return GeometryOperation.create(Geometry.class, SpatialOps.INTERSECTION, mixin, geometry);
    }

    public GeometryExpression<Geometry> union(Geometry geometry) {
        return union(ConstantImpl.create(geometry));
    }

    public GeometryExpression<Geometry> union(Expression<? extends Geometry> geometry) {
        return GeometryOperation.create(Geometry.class, SpatialOps.UNION, mixin, geometry);
    }

    public GeometryExpression<Geometry> difference(Geometry geometry) {
        return difference(ConstantImpl.create(geometry));
    }

    public GeometryExpression<Geometry> difference(Expression<? extends Geometry> geometry) {
        return GeometryOperation.create(Geometry.class, SpatialOps.DIFFERENCE, mixin, geometry);
    }

    public GeometryExpression<Geometry> symDifference(Geometry geometry) {
        return symDifference(ConstantImpl.create(geometry));
    }

    public GeometryExpression<Geometry> symDifference(Expression<? extends Geometry> geometry) {
        return GeometryOperation.create(Geometry.class, SpatialOps.SYMDIFFERENCE, mixin, geometry);
    }

}
