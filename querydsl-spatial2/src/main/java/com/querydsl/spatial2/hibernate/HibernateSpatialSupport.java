/*
 * Copyright 2020, The Querydsl Team (http://www.querydsl.com/team)
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
package com.querydsl.spatial2.hibernate;

import com.google.common.collect.Maps;
import com.querydsl.core.types.Operator;
import com.querydsl.spatial2.SpatialOps;


import java.util.Map;

/**
 * {@code HibernateSpatialSupport} provides mappings from operators to serialization templates
 * to be used in Hibernate Spatial
 */
public final class HibernateSpatialSupport {

    private HibernateSpatialSupport() { }

    public static Map<Operator, String> getSpatialOps() {
        Map<Operator, String> ops = Maps.newHashMap();
        ops.put(SpatialOps.DIMENSION, "dimension({0})");
        ops.put(SpatialOps.GEOMETRY_TYPE, "geometrytype({0}, {1})");
        ops.put(SpatialOps.SRID, "srid({0})");
        ops.put(SpatialOps.SRID2, "srid2({0}, {1})");
        ops.put(SpatialOps.ENVELOPE, "envelope({0})");
        ops.put(SpatialOps.AS_TEXT, "astext({0})");
        ops.put(SpatialOps.AS_BINARY, "asbinary({0})");
        ops.put(SpatialOps.IS_EMPTY, "isempty({0})");
        ops.put(SpatialOps.IS_SIMPLE, "issimple({0})");
        ops.put(SpatialOps.BOUNDARY, "boundary({0})");
        ops.put(SpatialOps.EXTENT, "extent({0})");

        ops.put(SpatialOps.EQUALS, "equals({0}, {1}) = true");
        ops.put(SpatialOps.DISJOINT, "disjoint({0}, {1}) = true");
        ops.put(SpatialOps.INTERSECTS, "intersects({0}, {1}) = true");
        ops.put(SpatialOps.TOUCHES, "touches({0}, {1}) = true");
        ops.put(SpatialOps.CROSSES, "crosses({0}, {1}) = true");
        ops.put(SpatialOps.WITHIN, "within({0}, {1}) = true");
        ops.put(SpatialOps.CONTAINS, "contains({0}, {1}) = true");
        ops.put(SpatialOps.OVERLAPS, "overlaps({0}, {1}) = true");
        ops.put(SpatialOps.RELATE, "relate({0}, {1}, {2}) = true");

        ops.put(SpatialOps.DISTANCE, "distance({0}, {1})");
        ops.put(SpatialOps.DISTANCE2, "distance({0}, {1}, {2})");
        ops.put(SpatialOps.DISTANCE_SPHERE, "distancesphere({0}, {1})");
        ops.put(SpatialOps.DISTANCE_SPHEROID, "distancespheroid({0}, {1})");

        ops.put(SpatialOps.BUFFER, "buffer({0}, {1})");
        ops.put(SpatialOps.BUFFER2, "buffer({0}, {1}, {2})");
        ops.put(SpatialOps.CONVEXHULL, "convexhull({0})");
        ops.put(SpatialOps.INTERSECTION, "intersection({0}, {1})");
        ops.put(SpatialOps.UNION, "geomunion({0}, {1})");
        ops.put(SpatialOps.DIFFERENCE, "difference({0}, {1})");
        ops.put(SpatialOps.SYMDIFFERENCE, "symdifference({0}, {1})");
        ops.put(SpatialOps.DWITHIN, "dwithin({0}, {1}, {2}) = true");
        ops.put(SpatialOps.TRANSFORM, "transform({0}, {1})");

        // custom
        ops.put(SpatialOps.WKTTOSQL, "wkttosql({0}, {1})");
        ops.put(SpatialOps.WKBTOSQL, "wkbtosql({0}, {1})");
        ops.put(SpatialOps.X, "x({0})");
        ops.put(SpatialOps.X2, "x({0}, {1})");
        ops.put(SpatialOps.Y, "y({0})");
        ops.put(SpatialOps.Y2, "y({0}, {1})");
        ops.put(SpatialOps.Z, "y({0})");
        ops.put(SpatialOps.Z2, "y({0}, {1})");
        ops.put(SpatialOps.M, "y({0})");
        ops.put(SpatialOps.M2, "y({0}, {1})");
        ops.put(SpatialOps.START_POINT, "startpoint({0})");
        ops.put(SpatialOps.END_POINT, "endpoint({0})");
        ops.put(SpatialOps.IS_RING, "isring({0})");
        ops.put(SpatialOps.LENGTH, "length({0})");
        ops.put(SpatialOps.LENGTH2, "length({0}, {1})");
        ops.put(SpatialOps.NUM_POINTS, "numpoints({0})");
        ops.put(SpatialOps.POINTN, "pointn({0})");
        ops.put(SpatialOps.AREA, "area({0})");
        ops.put(SpatialOps.AREA2, "area({0}, {1})");
        ops.put(SpatialOps.CENTROID, "centroid({0})");
        ops.put(SpatialOps.POINT_ON_SURFACE, "pointonsurface({0})");
        ops.put(SpatialOps.EXTERIOR_RING, "exteriorring({0})");
        ops.put(SpatialOps.EXTERIOR_RING2, "exteriorring({0}, {1})");
        ops.put(SpatialOps.INTERIOR_RINGS, "interiorrings({0})");
        ops.put(SpatialOps.INTERIOR_RINGS2, "interiorrings({0}, {1})");
        ops.put(SpatialOps.NUM_INTERIOR_RING, "numinteriorring({0})");
        ops.put(SpatialOps.INTERIOR_RINGN, "interiorringn({0}, {1})");
        ops.put(SpatialOps.GEOMETRIES, "geometries({0})");
        ops.put(SpatialOps.NUM_SURFACES, "numsurfaces({0})");
        ops.put(SpatialOps.SURFACE, "surface({0})");
        ops.put(SpatialOps.NUM_GEOMETRIES, "numgeometries({0})");
        ops.put(SpatialOps.GEOMETRYN, "geometryn({0})");
        ops.put(SpatialOps.IS_CLOSED, "isclosed({0})");
        ops.put(SpatialOps.AS_EWKT, "asewkt({0})");
        ops.put(SpatialOps.GEOM_FROM_TEXT, "geomfromtext({0})");
        ops.put(SpatialOps.SET_SRID, "setsrid({0}, {1})");
        ops.put(SpatialOps.XMIN, "xmin({0})");
        ops.put(SpatialOps.XMAX, "xmax({0})");
        ops.put(SpatialOps.YMIN, "ymin({0})");
        ops.put(SpatialOps.YMAX, "ymax({0})");
        ops.put(SpatialOps.COLLECT, "collect({0})");
        ops.put(SpatialOps.COLLECT2, "collect({0}, {1})");
        ops.put(SpatialOps.TRANSLATE, "translate({0})");
        ops.put(SpatialOps.TRANSLATE2, "translate({0}, {1})");
        return ops;
    }
}
