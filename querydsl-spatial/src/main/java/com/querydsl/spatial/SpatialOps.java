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
package com.querydsl.spatial;

import com.querydsl.core.types.Operator;

/**
 * {@code SpatialOps} provides {@link Operator} instances for spatial operations
 *
 * @author tiwe
 *
 */
public enum SpatialOps implements Operator {

    // Geometry
    DIMENSION(Integer.class),
    GEOMETRY_TYPE(String.class),
    AS_TEXT(String.class),
    AS_BINARY(Object.class),
    SRID(Integer.class),
    SRID2(Integer.class),
    IS_EMPTY(Boolean.class),
    IS_SIMPLE(Boolean.class),
    BOUNDARY(Object.class),
    ENVELOPE(Object.class),
    WKTTOSQL(Object.class),
    WKBTOSQL(Object.class),
    EQUALS(Boolean.class),
    DISJOINT(Boolean.class),
    INTERSECTS(Boolean.class),
    TOUCHES(Boolean.class),
    CROSSES(Boolean.class),
    WITHIN(Boolean.class),
    CONTAINS(Boolean.class),
    OVERLAPS(Boolean.class),
    RELATE(Boolean.class),
    DISTANCE(Number.class),
    DISTANCE2(Number.class),
    DISTANCE_SPHERE(Number.class),
    DISTANCE_SPHEROID(Number.class),
    INTERSECTION(Object.class),
    DIFFERENCE(Object.class),
    UNION(Object.class),
    SYMDIFFERENCE(Object.class),
    BUFFER(Object.class),
    BUFFER2(Object.class),
    CONVEXHULL(Object.class),
    TRANSFORM(Object.class),

    // Point
    X(Number.class),
    X2(Number.class),
    Y(Number.class),
    Y2(Number.class),
    Z(Number.class),
    Z2(Number.class),
    M(Number.class),
    M2(Number.class),

    // Curve
    START_POINT(Object.class),
    END_POINT(Object.class),
    IS_RING(Object.class),
    LENGTH(Object.class),
    LENGTH2(Object.class),

    // LineString
    NUM_POINTS(Integer.class),
    POINTN(Object.class),

    // Surface
    AREA(Number.class),
    AREA2(Number.class),
    CENTROID(Object.class),
    POINT_ON_SURFACE(Object.class),

    // Polygon
    EXTERIOR_RING(Object.class),
    EXTERIOR_RING2(Object.class),
    INTERIOR_RINGS(Object.class),
    INTERIOR_RINGS2(Object.class),
    NUM_INTERIOR_RING(Integer.class),
    INTERIOR_RINGN(Object.class),

    // Polyhedral Surface
    GEOMETRIES(Object.class),
    NUM_SURFACES(Integer.class),
    SURFACE(Object.class),

    // GeometryCollection
    NUM_GEOMETRIES(Integer.class),
    GEOMETRYN(Object.class),

    // MultiCurve
    IS_CLOSED(Boolean.class),

    // Extensions
    AS_EWKT(String.class),
    GEOM_FROM_TEXT(Object.class),
    SET_SRID(Object.class),
    XMIN(Number.class),
    XMAX(Number.class),
    YMIN(Number.class),
    YMAX(Number.class),
    DWITHIN(Boolean.class),
    EXTENT(Object.class),
    COLLECT(Object.class),
    COLLECT2(Object.class),
    TRANSLATE(Object.class),
    TRANSLATE2(Object.class);

    private final Class<?> type;

    private SpatialOps(Class<?> type) {
        this.type = type;
    }

    public Class<?> getType() {
        return type;
    }
}