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

import javax.annotation.Nullable;

import org.geolatte.geom.Geometry;

import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.*;

/**
 * Geometry is the root class of the hierarchy. Geometry is an abstract (non-instantiable) class.
 *
 * @author tiwe
 *
 * @param <T>
 */
public abstract class GeometryExpression<T extends Geometry> extends SimpleExpression<T> {

    private static final long serialVersionUID = -1183228394472681995L;

    @Nullable
    private transient volatile NumberExpression<Integer> dimension, coordinateDimension, spatialDimension, srid;

    @Nullable
    private transient volatile StringExpression geometryType;

    @Nullable
    private transient volatile StringExpression text;

    @Nullable
    private transient volatile GeometryExpression<Geometry> envelope, boundary, convexHull;

    @Nullable
    private transient volatile BooleanExpression empty, simple, threed, measured;

    private transient volatile SimpleExpression<byte[]> binary;

    public GeometryExpression(Expression<T> mixin) {
        super(mixin);
    }

    /**
     * The inherent dimension of this geometric object, which must be less than or equal
     * to the coordinate dimension. In non-homogeneous collections, this will return the largest topological
     * dimension of the contained objects.
     *
     * @return dimension
     */
    public NumberExpression<Integer> dimension() {
        if (dimension == null) {
            dimension = Expressions.numberOperation(Integer.class, SpatialOps.DIMENSION, mixin);
        }
        return dimension;
    }

    /**
     * Returns the name of the instantiable subtype of Geometry of which this
     * geometric object is an instantiable member. The name of the subtype of Geometry is returned as a string.
     *
     * @return geometry type
     */
    public StringExpression geometryType() {
        if (geometryType == null) {
            geometryType = Expressions.stringOperation(SpatialOps.GEOMETRY_TYPE, mixin);
        }
        return geometryType;
    }

    /**
     * Returns the Spatial Reference System ID for this geometric object. This will normally be a
     * foreign key to an index of reference systems stored in either the same or some other datastore.
     *
     * @return SRID
     */
    public NumberExpression<Integer> srid() {
        if (srid == null) {
            srid = Expressions.numberOperation(Integer.class, SpatialOps.SRID, mixin);
        }
        return srid;
    }

    /**
     * The minimum bounding box for this Geometry, returned as a Geometry. The
     * polygon is defined by the corner points of the bounding box [(MINX, MINY), (MAXX, MINY), (MAXX, MAXY),
     * (MINX, MAXY), (MINX, MINY)]. Minimums for Z and M may be added. The simplest representation of an
     * Envelope is as two direct positions, one containing all the minimums, and another all the maximums. In some
     * cases, this coordinate will be outside the range of validity for the Spatial Reference System.
     *
     * @return envelope
     */
    public GeometryExpression<Geometry> envelope() {
        if (envelope == null) {
            envelope = GeometryExpressions.geometryOperation(SpatialOps.ENVELOPE, mixin);
        }
        return envelope;
    }

    /**
     * Exports this geometric object to a specific Well-known Text Representation of Geometry.
     *
     * @return text representation
     */
    public StringExpression asText() {
        if (text == null) {
            text = Expressions.stringOperation(SpatialOps.AS_TEXT, mixin);
        }
        return text;
    }

    /**
     * Exports this geometric object to a specific Well-known Binary Representation of
     * Geometry.
     *
     * @return binary representation
     */
    public SimpleExpression<byte[]> asBinary() {
        if (binary == null) {
            binary = Expressions.operation(byte[].class, SpatialOps.AS_BINARY, mixin);
        }
        return binary;
    }

    /**
     * Returns 1 (TRUE) if this geometric object is the empty Geometry. If true, then this
     * geometric object represents the empty point set ∅ for the coordinate space.
     *
     * @return empty
     */
    public BooleanExpression isEmpty() {
        if (empty == null) {
            empty = Expressions.booleanOperation(SpatialOps.IS_EMPTY, mixin);
        }
        return empty;
    }

    /**
     * Returns 1 (TRUE) if this geometric object has no anomalous geometric points, such
     * as self intersection or self tangency. The description of each instantiable geometric class
     * will include the specific conditions that cause an instance of that class to be classified as not simple.
     *
     * @return simple
     */
    public BooleanExpression isSimple() {
        if (simple == null) {
            simple = Expressions.booleanOperation(SpatialOps.IS_SIMPLE, mixin);
        }
        return simple;
    }

    /**
     * Returns the closure of the combinatorial boundary of this geometric object
     *
     * @return boundary
     */
    public GeometryExpression<Geometry> boundary() {
        if (boundary == null) {
            boundary = GeometryExpressions.geometryOperation(SpatialOps.BOUNDARY, mixin);
        }
        return boundary;
    }

    // query

    /* (non-Javadoc)
     * @see com.querydsl.core.types.dsl.SimpleExpression#eq(java.lang.Object)
     */
    @Override
    public BooleanExpression eq(Geometry right) {
        return eq(ConstantImpl.create(right));
    }

    /* (non-Javadoc)
     * @see com.querydsl.core.types.dsl.SimpleExpression#eq(com.querydsl.core.types.Expression)
     */
    @Override
    public BooleanExpression eq(Expression<? super T> right) {
        return Expressions.booleanOperation(SpatialOps.EQUALS, mixin, right);
    }

    /**
     * Returns 1 (TRUE) if this geometric object is “spatially disjoint” from anotherGeometry.
     *
     * @param geometry other geometry
     * @return true, if disjoint
     */
    public BooleanExpression disjoint(Geometry geometry) {
        return disjoint(ConstantImpl.create(geometry));
    }

    /**
     * Returns 1 (TRUE) if this geometric object is “spatially disjoint” from anotherGeometry.
     *
     * @param geometry other geometry
     * @return true, if disjoint
     */
    public BooleanExpression disjoint(Expression<? extends Geometry> geometry) {
        return Expressions.booleanOperation(SpatialOps.DISJOINT, mixin, geometry);
    }

    /**
     * Returns 1 (TRUE) if this geometric object “spatially intersects” anotherGeometry.
     *
     * @param geometry other geometry
     * @return true, if intersects
     */
    public BooleanExpression intersects(Geometry geometry) {
        return intersects(ConstantImpl.create(geometry));
    }

    /**
     * Returns 1 (TRUE) if this geometric object “spatially intersects” anotherGeometry.
     *
     * @param geometry other geometry
     * @return true, if intersects
     */
    public BooleanExpression intersects(Expression<? extends Geometry> geometry) {
        return Expressions.booleanOperation(SpatialOps.INTERSECTS, mixin, geometry);
    }

    /**
     * Returns 1 (TRUE) if this geometric object “spatially touches” anotherGeometry.
     *
     * @param geometry other geometry
     * @return true, if touches
     */
    public BooleanExpression touches(Geometry geometry) {
        return touches(ConstantImpl.create(geometry));
    }

    /**
     * Returns 1 (TRUE) if this geometric object “spatially touches” anotherGeometry.
     *
     * @param geometry other geometry
     * @return true, if touches
     */
    public BooleanExpression touches(Expression<? extends Geometry> geometry) {
        return Expressions.booleanOperation(SpatialOps.TOUCHES, mixin, geometry);
    }

    /**
     * Returns 1 (TRUE) if this geometric object “spatially crosses’ anotherGeometry.
     *
     * @param geometry other geometry
     * @return trye, if crosses
     */
    public BooleanExpression crosses(Geometry geometry) {
         return crosses(ConstantImpl.create(geometry));
    }

    /**
     * Returns 1 (TRUE) if this geometric object “spatially crosses’ anotherGeometry.
     *
     * @param geometry other geometry
     * @return true, if crosses
     */
    public BooleanExpression crosses(Expression<? extends Geometry> geometry) {
        return Expressions.booleanOperation(SpatialOps.CROSSES, mixin, geometry);
    }

    /**
     * Returns 1 (TRUE) if this geometric object is “spatially within” anotherGeometry.
     *
     * @param geometry other geometry
     * @return true, if within
     */
    public BooleanExpression within(Geometry geometry) {
        return within(ConstantImpl.create(geometry));
    }

    /**
     * Returns 1 (TRUE) if this geometric object is “spatially within” anotherGeometry.
     *
     * @param geometry other geometry
     * @return true, if within
     */
    public BooleanExpression within(Expression<? extends Geometry> geometry) {
        return Expressions.booleanOperation(SpatialOps.WITHIN, mixin, geometry);
    }

    /**
     * Returns 1 (TRUE) if this geometric object “spatially contains” anotherGeometry.
     *
     * @param geometry other geometry
     * @return true, if contains
     */
    public BooleanExpression contains(Geometry geometry) {
        return contains(ConstantImpl.create(geometry));
    }

    /**
     * Returns 1 (TRUE) if this geometric object “spatially contains” anotherGeometry.
     *
     * @param geometry other geometry
     * @return true, if contains
     */
    public BooleanExpression contains(Expression<? extends Geometry> geometry) {
        return Expressions.booleanOperation(SpatialOps.CONTAINS, mixin, geometry);
    }

    /**
     * Returns 1 (TRUE) if this geometric object “spatially overlaps” anotherGeometry.
     *
     * @param geometry other geometry
     * @return true, if overlaps
     */
    public BooleanExpression overlaps(Geometry geometry) {
        return overlaps(ConstantImpl.create(geometry));
    }

    /**
     * Returns 1 (TRUE) if this geometric object “spatially overlaps” anotherGeometry.
     *
     * @param geometry other geometry
     * @return true, if overlaps
     */
    public BooleanExpression overlaps(Expression<? extends Geometry> geometry) {
        return Expressions.booleanOperation(SpatialOps.OVERLAPS, mixin, geometry);
    }

    /**
     * Returns 1 (TRUE) if this geometric object is spatially related to anotherGeometry by testing
     * for intersections between the interior, boundary and exterior of the two geometric objects
     * as specified by the values in the intersectionPatternMatrix. This returns FALSE if all the
     * tested intersections are empty except exterior (this) intersect exterior (another).
     *
     * @param geometry other geometry
     * @param matrix matrix
     * @return true, if this geometry is spatially related to the other
     */
    public BooleanExpression relate(Geometry geometry, String matrix) {
        return relate(ConstantImpl.create(geometry), matrix);
    }

    /**
     * Returns 1 (TRUE) if this geometric object is spatially related to anotherGeometry by testing
     * for intersections between the interior, boundary and exterior of the two geometric objects
     * as specified by the values in the intersectionPatternMatrix. This returns FALSE if all the
     * tested intersections are empty except exterior (this) intersect exterior (another).
     *
     * @param geometry other geometry
     * @param matrix matrix
     * @return true, if this geometry is spatially related to the other
     */
    public BooleanExpression relate(Expression<? extends Geometry> geometry, String matrix) {
        return Expressions.booleanOperation(SpatialOps.RELATE, mixin, geometry, ConstantImpl.create(matrix));
    }

    // analysis

    /**
     * Returns the shortest distance between any two Points in the two geometric objects as
     * calculated in the spatial reference system of this geometric object. Because the geometries
     * are closed, it is possible to find a point on each geometric object involved, such that the
     * distance between these 2 points is the returned distance between their geometric objects.
     *
     * @param geometry other geometry
     * @return distance between this and the other geometry
     */
    public NumberExpression<Double> distance(Geometry geometry) {
        return distance(ConstantImpl.create(geometry));
    }

    /**
     * Returns the shortest distance between any two Points in the two geometric objects as
     * calculated in the spatial reference system of this geometric object. Because the geometries
     * are closed, it is possible to find a point on each geometric object involved, such that the
     * distance between these 2 points is the returned distance between their geometric objects.
     *
     * @param geometry other geometry
     * @return distance between this and the other geometry
     */
    public NumberExpression<Double> distance(Expression<? extends Geometry> geometry) {
        return Expressions.numberOperation(Double.class, SpatialOps.DISTANCE, mixin, geometry);
    }

    // TODO maybe move out
    public NumberExpression<Double> distanceSphere(Expression<? extends Geometry> geometry) {
        return Expressions.numberOperation(Double.class, SpatialOps.DISTANCE_SPHERE, mixin, geometry);
    }

    // TODO maybe move out
    public NumberExpression<Double> distanceSpheroid(Expression<? extends Geometry> geometry) {
        return Expressions.numberOperation(Double.class, SpatialOps.DISTANCE_SPHEROID, mixin, geometry);
    }

    /**
     * Returns a geometric object that represents all Points whose distance from this geometric
     * object is less than or equal to distance. Calculations are in the spatial reference system
     * of this geometric object. Because of the limitations of linear interpolation, there will
     * often be some relatively small error in this distance, but it should be near the resolution
     * of the coordinates used.
     *
     * @param distance distance
     * @return buffer
     */
    public GeometryExpression<Geometry> buffer(double distance) {
        return GeometryExpressions.geometryOperation(SpatialOps.BUFFER, mixin, ConstantImpl.create(distance));
    }

    /**
     * Returns a geometric object that represents the convex hull of this geometric object.
     * Convex hulls, being dependent on straight lines, can be accurately represented in linear
     * interpolations for any geometry restricted to linear interpolations.
     *
     * @return convex hull
     */
    public GeometryExpression<Geometry> convexHull() {
        if (convexHull == null) {
            convexHull = GeometryExpressions.geometryOperation(SpatialOps.CONVEXHULL, mixin);
        }
        return convexHull;
    }

    /**
     * Returns a geometric object that represents the Point set intersection of this geometric
     * object with anotherGeometry.
     *
     * @param geometry other geometry
     * @return intersection of this and the other geometry
     */
    public GeometryExpression<Geometry> intersection(Geometry geometry) {
        return intersection(ConstantImpl.create(geometry));
    }

    /**
     * Returns a geometric object that represents the Point set intersection of this geometric
     * object with anotherGeometry.
     *
     * @param geometry other geometry
     * @return intersection of this and the other geometry
     */
    public GeometryExpression<Geometry> intersection(Expression<? extends Geometry> geometry) {
        return GeometryExpressions.geometryOperation(SpatialOps.INTERSECTION, mixin, geometry);
    }

    /**
     * Returns a geometric object that represents the Point set
     * union of this geometric object with anotherGeometry.
     *
     * @param geometry other geometry
     * @return union of this and the other geometry
     */
    public GeometryExpression<Geometry> union(Geometry geometry) {
        return union(ConstantImpl.create(geometry));
    }

    /**
     * Returns a geometric object that represents the Point set
     * union of this geometric object with anotherGeometry.
     *
     * @param geometry other geometry
     * @return union of this and the other geometry
     */
    public GeometryExpression<Geometry> union(Expression<? extends Geometry> geometry) {
        return GeometryExpressions.geometryOperation(SpatialOps.UNION, mixin, geometry);
    }

    /**
     * Returns a geometric object that represents the Point
     * set difference of this geometric object with anotherGeometry.
     *
     * @param geometry other geometry
     * @return difference between this and the other geometry
     */
    public GeometryExpression<Geometry> difference(Geometry geometry) {
        return difference(ConstantImpl.create(geometry));
    }

    /**
     * Returns a geometric object that represents the Point
     * set difference of this geometric object with anotherGeometry.
     *
     * @param geometry other geometry
     * @return difference between this and the other geometry
     */
    public GeometryExpression<Geometry> difference(Expression<? extends Geometry> geometry) {
        return GeometryExpressions.geometryOperation(SpatialOps.DIFFERENCE, mixin, geometry);
    }

    /**
     * Returns a geometric object that represents the
     * Point set symmetric difference of this geometric object with anotherGeometry.
     *
     * @param geometry other geometry
     * @return symmetric difference between this and the other geometry
     */
    public GeometryExpression<Geometry> symDifference(Geometry geometry) {
        return symDifference(ConstantImpl.create(geometry));
    }

    /**
     * Returns a geometric object that represents the
     * Point set symmetric difference of this geometric object with anotherGeometry.
     *
     * @param geometry other geometry
     * @return symmetric difference between this and the geometry
     */
    public GeometryExpression<Geometry> symDifference(Expression<? extends Geometry> geometry) {
        return GeometryExpressions.geometryOperation(SpatialOps.SYMDIFFERENCE, mixin, geometry);
    }

    public GeometryExpression<Geometry> transform(int srid) {
        return GeometryExpressions.geometryOperation(SpatialOps.TRANSFORM, mixin, ConstantImpl.create(srid));
    }

}
