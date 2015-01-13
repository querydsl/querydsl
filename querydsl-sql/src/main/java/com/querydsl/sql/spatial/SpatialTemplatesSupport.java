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
package com.querydsl.sql.spatial;

import java.util.Map;

import com.google.common.collect.Maps;
import com.querydsl.spatial.SpatialOps;
import com.querydsl.core.types.Operator;

/**
 * Static factory methods for spatial enabled SQLTemplates subclasses
 *
 * @author tiwe
 *
 */
public final class SpatialTemplatesSupport {

    private static String createSpatial(String name, int args, boolean asFunction) {
        StringBuilder result = new StringBuilder();
        if (!asFunction) {
            result.append("{0}.");
        }
        result.append(name);
        result.append("(");
        int start = asFunction ? 0 : 1;
        for (int i = start; i < args; i++) {
            if (i > start) {
                result.append(", ");
            }
            result.append("{" + i + "}");
        }
        result.append(")");
        return result.toString();
    }

    public static Map<Operator<?>, String> getSpatialOps(boolean asFunction) {
        return getSpatialOps("ST_", asFunction);
    }

    public static Map<Operator<?>, String> getSpatialOps(String prefix, boolean asFunction) {
        Map<Operator<?>, String> ops = Maps.newHashMap();
        ops.put(SpatialOps.AREA, createSpatial(prefix + "Area", 1, asFunction));
        ops.put(SpatialOps.AREA2, createSpatial(prefix + "Area", 2, asFunction));
        ops.put(SpatialOps.AS_BINARY, createSpatial(prefix + "AsBinary", 1, asFunction));
        ops.put(SpatialOps.AS_TEXT, createSpatial(prefix + "AsText", 1, asFunction));
        ops.put(SpatialOps.BOUNDARY, createSpatial(prefix + "Boundary", 1, asFunction));
        ops.put(SpatialOps.BUFFER, createSpatial(prefix + "Buffer", 2, asFunction));
        ops.put(SpatialOps.BUFFER2, createSpatial(prefix + "Buffer", 3, asFunction));
        ops.put(SpatialOps.CENTROID, createSpatial(prefix + "Centroid", 1, asFunction));
        ops.put(SpatialOps.CONTAINS, createSpatial(prefix + "Contains", 2, asFunction));
        ops.put(SpatialOps.CONVEXHULL, createSpatial(prefix + "ConvexHull", 1, asFunction));
        ops.put(SpatialOps.CROSSES, createSpatial(prefix + "Crosses", 2, asFunction));
        ops.put(SpatialOps.DIFFERENCE, createSpatial(prefix + "Difference", 2, asFunction));
        ops.put(SpatialOps.DIMENSION, createSpatial(prefix + "Dimension", 1, asFunction));
        ops.put(SpatialOps.DISJOINT, createSpatial(prefix + "Disjoint", 2, asFunction));
        ops.put(SpatialOps.DISTANCE, createSpatial(prefix + "Distance", 2, asFunction));
        ops.put(SpatialOps.DISTANCE2, createSpatial(prefix + "Distance", 3, asFunction));
        ops.put(SpatialOps.END_POINT, createSpatial(prefix + "EndPoint", 1, asFunction));
        ops.put(SpatialOps.ENVELOPE, createSpatial(prefix + "Envelope", 1, asFunction));
        ops.put(SpatialOps.EQUALS, createSpatial(prefix + "Equals", 2, asFunction));
        ops.put(SpatialOps.EXTERIOR_RING, createSpatial(prefix + "ExteriorRing", 1, asFunction));
        ops.put(SpatialOps.EXTERIOR_RING2, createSpatial(prefix + "ExteriorRing", 2, asFunction));
        ops.put(SpatialOps.GEOMETRIES, createSpatial(prefix + "Geometries", 1, asFunction));
        ops.put(SpatialOps.GEOMETRY_TYPE, createSpatial(prefix + "GeometryType", 1, asFunction));
        ops.put(SpatialOps.GEOMETRYN, createSpatial(prefix + "GeometryN", 2, asFunction));
        ops.put(SpatialOps.INTERIOR_RINGN, createSpatial(prefix + "InteriorRingN", 2, asFunction));
        ops.put(SpatialOps.INTERIOR_RINGS, createSpatial(prefix + "InteriorRings", 1, asFunction));
        ops.put(SpatialOps.INTERIOR_RINGS2, createSpatial(prefix + "InteriorRings", 2, asFunction));
        ops.put(SpatialOps.INTERSECTION, createSpatial(prefix + "Intersection", 2, asFunction));
        ops.put(SpatialOps.INTERSECTS, createSpatial(prefix + "Intersects", 2, asFunction));
        ops.put(SpatialOps.IS_CLOSED, createSpatial(prefix + "IsClosed", 1, asFunction));
        ops.put(SpatialOps.IS_EMPTY, createSpatial(prefix + "IsEmpty", 1, asFunction));
        ops.put(SpatialOps.IS_RING, createSpatial(prefix + "IsRing", 1, asFunction));
        ops.put(SpatialOps.IS_SIMPLE, createSpatial(prefix + "IsSimple", 1, asFunction));
        ops.put(SpatialOps.LENGTH, createSpatial(prefix + "Length", 1, asFunction));
        ops.put(SpatialOps.LENGTH2, createSpatial(prefix + "Length", 2, asFunction));
        ops.put(SpatialOps.M, createSpatial(prefix + "M", 1, asFunction));
        ops.put(SpatialOps.M2, createSpatial(prefix + "M", 2, asFunction));
        ops.put(SpatialOps.NUM_GEOMETRIES, createSpatial(prefix + "NumGeometries", 1, asFunction));
        ops.put(SpatialOps.NUM_INTERIOR_RING, createSpatial(prefix + "NumInteriorRing", 1, asFunction));
        ops.put(SpatialOps.NUM_POINTS, createSpatial(prefix + "NumPoints", 1, asFunction));
        ops.put(SpatialOps.NUM_SURFACES, createSpatial(prefix + "NumSurfaces", 1, asFunction));
        ops.put(SpatialOps.OVERLAPS, createSpatial(prefix + "Overlaps", 2, asFunction));
        ops.put(SpatialOps.POINT_ON_SURFACE, createSpatial(prefix + "PointOnSurface", 1, asFunction));
        ops.put(SpatialOps.POINTN, createSpatial(prefix + "PointN", 2, asFunction));
        ops.put(SpatialOps.RELATE, createSpatial(prefix + "Relate", 3, asFunction));
        ops.put(SpatialOps.SRID, createSpatial(prefix + "SRID", 1, asFunction));
        ops.put(SpatialOps.SRID2, createSpatial(prefix + "SRID", 2, asFunction));
        ops.put(SpatialOps.START_POINT, createSpatial(prefix + "StartPoint", 1, asFunction));
        ops.put(SpatialOps.SURFACE, createSpatial(prefix + "Surface", 1, asFunction)); // XXX
        ops.put(SpatialOps.SYMDIFFERENCE, createSpatial(prefix + "SymDifference", 2, asFunction));
        ops.put(SpatialOps.TOUCHES, createSpatial(prefix + "Touches", 2, asFunction));
        ops.put(SpatialOps.TRANSFORM, createSpatial(prefix + "Transform", 2, asFunction));
        ops.put(SpatialOps.UNION, createSpatial(prefix + "Union", 2, asFunction));
        ops.put(SpatialOps.WITHIN, createSpatial(prefix + "Within", 2, asFunction));
        ops.put(SpatialOps.WKBTOSQL, createSpatial(prefix + "WKBToSQL", 2, asFunction));
        ops.put(SpatialOps.WKTTOSQL, createSpatial(prefix + "WKTToSQL", 2, asFunction));
        ops.put(SpatialOps.X, createSpatial(prefix + "X", 1, asFunction));
        ops.put(SpatialOps.X2, createSpatial(prefix + "X", 2, asFunction));
        ops.put(SpatialOps.Y, createSpatial(prefix + "Y", 1, asFunction));
        ops.put(SpatialOps.Y2, createSpatial(prefix + "Y", 2, asFunction));
        ops.put(SpatialOps.Z, createSpatial(prefix + "Z", 1, asFunction));
        ops.put(SpatialOps.Z2, createSpatial(prefix + "Z", 2, asFunction));

        // extensions
        ops.put(SpatialOps.AS_EWKT, createSpatial(prefix + "AsEWKT", 1, asFunction));
        ops.put(SpatialOps.GEOM_FROM_TEXT, createSpatial(prefix + "GeomFromText", 1, asFunction));
        ops.put(SpatialOps.SET_SRID, createSpatial(prefix + "SetSRID", 2, asFunction));
        ops.put(SpatialOps.XMIN, createSpatial(prefix + "XMin", 1, asFunction));
        ops.put(SpatialOps.XMAX, createSpatial(prefix + "XMax", 1, asFunction));
        ops.put(SpatialOps.YMIN, createSpatial(prefix + "YMin", 1, asFunction));
        ops.put(SpatialOps.YMAX, createSpatial(prefix + "YMax", 1, asFunction));
        ops.put(SpatialOps.DWITHIN, createSpatial(prefix + "DWithin", 3, asFunction));
        ops.put(SpatialOps.EXTENT, createSpatial(prefix + "Extent", 1, asFunction));
        ops.put(SpatialOps.COLLECT, createSpatial(prefix + "Collect", 1, asFunction));
        ops.put(SpatialOps.COLLECT2, createSpatial(prefix + "Collect", 2, asFunction));
        ops.put(SpatialOps.TRANSLATE, createSpatial(prefix + "Translate", 3, asFunction));
        ops.put(SpatialOps.TRANSLATE2, createSpatial(prefix + "Translate", 4, asFunction));

        return ops;
    }

}
