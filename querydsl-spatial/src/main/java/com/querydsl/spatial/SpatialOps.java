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
 * @author tiwe
 *
 */
public enum SpatialOps implements Operator {

    // Geometry
    DIMENSION,
    GEOMETRY_TYPE,
    AS_TEXT,
    AS_BINARY,
    SRID,
    SRID2,
    IS_EMPTY,
    IS_SIMPLE,
    BOUNDARY,
    ENVELOPE,
    WKTTOSQL,
    WKBTOSQL,
    EQUALS,
    DISJOINT,
    INTERSECTS,
    TOUCHES,
    CROSSES,
    WITHIN,
    CONTAINS,
    OVERLAPS,
    RELATE,
    DISTANCE,
    DISTANCE2,
    DISTANCE_SPHERE,
    DISTANCE_SPHEROID,
    INTERSECTION,
    DIFFERENCE,
    UNION,
    SYMDIFFERENCE,
    BUFFER,
    BUFFER2,
    CONVEXHULL,
    TRANSFORM,

    // Point
    X,
    X2,
    Y,
    Y2,
    Z,
    Z2,
    M,
    M2,

    // Curve
    START_POINT,
    END_POINT,
    IS_RING,
    LENGTH,
    LENGTH2,

    // LineString
    NUM_POINTS,
    POINTN,

    // Surface
    AREA,
    AREA2,
    CENTROID,
    POINT_ON_SURFACE,

    // Polygon
    EXTERIOR_RING,
    EXTERIOR_RING2,
    INTERIOR_RINGS,
    INTERIOR_RINGS2,
    NUM_INTERIOR_RING,
    INTERIOR_RINGN,

    // Polyhedral Surface
    GEOMETRIES,
    NUM_SURFACES,
    SURFACE,

    // GeometryCollection
    NUM_GEOMETRIES,
    GEOMETRYN,

    // MultiCurve
    IS_CLOSED,

    // Extensions
    AS_EWKT,
    GEOM_FROM_TEXT,
    SET_SRID,
    XMIN,
    XMAX,
    YMIN,
    YMAX,
    DWITHIN,
    EXTENT,
    COLLECT,
    COLLECT2,
    TRANSLATE,
    TRANSLATE2;
}