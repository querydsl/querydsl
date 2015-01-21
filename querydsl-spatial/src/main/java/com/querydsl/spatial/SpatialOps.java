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
import com.querydsl.core.types.OperatorImpl;

/**
 * @author tiwe
 *
 */
public final class SpatialOps {

        private static final String NS = SpatialOps.class.getName();

        // Geometry
        public static final Operator<Object> DIMENSION = new OperatorImpl<Object>(NS, "DIMENSION");
        public static final Operator<Object> GEOMETRY_TYPE = new OperatorImpl<Object>(NS, "GEOMETRY_TYPE");
        public static final Operator<Object> AS_TEXT = new OperatorImpl<Object>(NS, "AS_TEXT");
        public static final Operator<Object> AS_BINARY = new OperatorImpl<Object>(NS, "AS_BINARY");
        public static final Operator<Object> SRID = new OperatorImpl<Object>(NS, "SRID");
        public static final Operator<Object> SRID2 = new OperatorImpl<Object>(NS, "SRID2");
        public static final Operator<Object> IS_EMPTY = new OperatorImpl<Object>(NS, "IS_EMPTY");
        public static final Operator<Object> IS_SIMPLE = new OperatorImpl<Object>(NS, "IS_SIMPLE");
        public static final Operator<Object> BOUNDARY = new OperatorImpl<Object>(NS, "BOUNDARY");
        public static final Operator<Object> ENVELOPE = new OperatorImpl<Object>(NS, "ENVELOPE");
        public static final Operator<Object> WKTTOSQL = new OperatorImpl<Object>(NS, "WKTTOSQL");
        public static final Operator<Object> WKBTOSQL = new OperatorImpl<Object>(NS, "WKBTOSQL");
        public static final Operator<Object> EQUALS = new OperatorImpl<Object>(NS, "EQUALS");
        public static final Operator<Object> DISJOINT = new OperatorImpl<Object>(NS, "DISJOINT");
        public static final Operator<Object> INTERSECTS = new OperatorImpl<Object>(NS, "INTERSECTS");
        public static final Operator<Object> TOUCHES = new OperatorImpl<Object>(NS, "TOUCHES");
        public static final Operator<Object> CROSSES = new OperatorImpl<Object>(NS, "CROSSES");
        public static final Operator<Object> WITHIN = new OperatorImpl<Object>(NS, "WITHIN");
        public static final Operator<Object> CONTAINS = new OperatorImpl<Object>(NS, "CONTAINS");
        public static final Operator<Object> OVERLAPS = new OperatorImpl<Object>(NS, "OVERLAPS");
        public static final Operator<Object> RELATE = new OperatorImpl<Object>(NS, "RELATE");
        public static final Operator<Object> DISTANCE = new OperatorImpl<Object>(NS, "DISTANCE");
        public static final Operator<Object> DISTANCE2 = new OperatorImpl<Object>(NS, "DISTANCE2");
        public static final Operator<Object> DISTANCE_SPHERE = new OperatorImpl<Object>(NS, "DISTANCE_SPHERE");
        public static final Operator<Object> DISTANCE_SPHEROID = new OperatorImpl<Object>(NS, "DISTANCE_SPHEROID");
        public static final Operator<Object> INTERSECTION = new OperatorImpl<Object>(NS, "INTERSECTION");
        public static final Operator<Object> DIFFERENCE = new OperatorImpl<Object>(NS, "DIFFERENCE");
        public static final Operator<Object> UNION = new OperatorImpl<Object>(NS, "UNION");
        public static final Operator<Object> SYMDIFFERENCE = new OperatorImpl<Object>(NS, "SYMDIFFERENCE");
        public static final Operator<Object> BUFFER = new OperatorImpl<Object>(NS, "BUFFER");
        public static final Operator<Object> BUFFER2 = new OperatorImpl<Object>(NS, "BUFFER2");
        public static final Operator<Object> CONVEXHULL = new OperatorImpl<Object>(NS, "CONVEXHULL");
        public static final Operator<Object> TRANSFORM = new OperatorImpl<Object>(NS, "TRANSFORM");

        // Point
        public static final Operator<Object> X = new OperatorImpl<Object>(NS, "X");
        public static final Operator<Object> X2 = new OperatorImpl<Object>(NS, "X2");
        public static final Operator<Object> Y = new OperatorImpl<Object>(NS, "Y");
        public static final Operator<Object> Y2 = new OperatorImpl<Object>(NS, "Y2");
        public static final Operator<Object> Z = new OperatorImpl<Object>(NS, "Z");
        public static final Operator<Object> Z2 = new OperatorImpl<Object>(NS, "Z2");
        public static final Operator<Object> M = new OperatorImpl<Object>(NS, "M");
        public static final Operator<Object> M2 = new OperatorImpl<Object>(NS, "M2");

        // Curve
        public static final Operator<Object> START_POINT = new OperatorImpl<Object>(NS, "START_POINT");
        public static final Operator<Object> END_POINT = new OperatorImpl<Object>(NS, "END_POINT");
        public static final Operator<Object> IS_RING = new OperatorImpl<Object>(NS, "IS_RING");
        public static final Operator<Object> LENGTH = new OperatorImpl<Object>(NS, "LENGTH");
        public static final Operator<Object> LENGTH2 = new OperatorImpl<Object>(NS, "LENGTH2");

        //LineString
        public static final Operator<Object> NUM_POINTS = new OperatorImpl<Object>(NS, "NUM_POINTS");
        public static final Operator<Object> POINTN = new OperatorImpl<Object>(NS, "POINTN");

        // Surface
        public static final Operator<Object> AREA = new OperatorImpl<Object>(NS, "AREA");
        public static final Operator<Object> AREA2 = new OperatorImpl<Object>(NS, "AREA2");
        public static final Operator<Object> CENTROID = new OperatorImpl<Object>(NS, "CENTROID");
        public static final Operator<Object> POINT_ON_SURFACE = new OperatorImpl<Object>(NS, "POINT_ON_SURFACE");

        // Polygon
        public static final Operator<Object> EXTERIOR_RING = new OperatorImpl<Object>(NS, "EXTERIOR_RING");
        public static final Operator<Object> EXTERIOR_RING2 = new OperatorImpl<Object>(NS, "EXTERIOR_RING2");
        public static final Operator<Object> INTERIOR_RINGS = new OperatorImpl<Object>(NS, "INTERIOR_RINGS");
        public static final Operator<Object> INTERIOR_RINGS2 = new OperatorImpl<Object>(NS, "INTERIOR_RINGS2");
        public static final Operator<Object> NUM_INTERIOR_RING = new OperatorImpl<Object>(NS, "NUM_INTERIOR_RING");
        public static final Operator<Object> INTERIOR_RINGN = new OperatorImpl<Object>(NS, "INTERIOR_RINGN");

        // Polyhedral Surface
        public static final Operator<Object> GEOMETRIES = new OperatorImpl<Object>(NS, "GEOMETRIES");
        public static final Operator<Object> NUM_SURFACES = new OperatorImpl<Object>(NS, "NUM_SURFACES");
        public static final Operator<Object> SURFACE = new OperatorImpl<Object>(NS, "SURFACE");

        // GeometryCollection
        public static final Operator<Object> NUM_GEOMETRIES = new OperatorImpl<Object>(NS, "NUM_GEOMETRIES");
        public static final Operator<Object> GEOMETRYN = new OperatorImpl<Object>(NS, "GEOMETRYN");

        // MultiCurve
        public static final Operator<Object> IS_CLOSED = new OperatorImpl<Object>(NS, "IS_CLOSED");

        // Extensions
        public static final Operator<Object> AS_EWKT = new OperatorImpl<Object>(NS, "AS_EWKT");
        public static final Operator<Object> GEOM_FROM_TEXT = new OperatorImpl<Object>(NS, "GEOM_FROM_TEXT");
        public static final Operator<Object> SET_SRID = new OperatorImpl<Object>(NS, "SET_SRID");
        public static final Operator<Object> XMIN = new OperatorImpl<Object>(NS, "XMIN");
        public static final Operator<Object> XMAX = new OperatorImpl<Object>(NS, "XMAX");
        public static final Operator<Object> YMIN = new OperatorImpl<Object>(NS, "YMIN");
        public static final Operator<Object> YMAX = new OperatorImpl<Object>(NS, "YMAX");
        public static final Operator<Object> DWITHIN = new OperatorImpl<Object>(NS, "DWITHIN");
        public static final Operator<Object> EXTENT = new OperatorImpl<Object>(NS, "EXTENT");
        public static final Operator<Object> COLLECT = new OperatorImpl<Object>(NS, "COLLECT");
        public static final Operator<Object> COLLECT2 = new OperatorImpl<Object>(NS, "COLLECT2");
        public static final Operator<Object> TRANSLATE = new OperatorImpl<Object>(NS, "TRANSLATE");
        public static final Operator<Object> TRANSLATE2 = new OperatorImpl<Object>(NS, "TRANSLATE2");

    }