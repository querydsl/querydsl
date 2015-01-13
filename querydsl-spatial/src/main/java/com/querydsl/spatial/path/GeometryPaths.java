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
package com.querydsl.spatial.path;

import org.geolatte.geom.Geometry;
import org.geolatte.geom.GeometryCollection;
import org.geolatte.geom.LineString;
import org.geolatte.geom.LinearRing;
import org.geolatte.geom.MultiLineString;
import org.geolatte.geom.MultiPoint;
import org.geolatte.geom.MultiPolygon;
import org.geolatte.geom.Point;
import org.geolatte.geom.PolyHedralSurface;
import org.geolatte.geom.Polygon;

/**
 * @author tiwe
 *
 */
public interface GeometryPaths {

    <A extends GeometryCollection> GeometryCollectionPath<A> createGeometryCollection(String property, Class<? extends A> type);

    <A extends Geometry> GeometryPath<A> createGeometry(String property, Class<? extends A> type);

    <A extends LinearRing> LinearRingPath<A> createLinearRing(String property, Class<? extends A> type);

    <A extends LineString> LineStringPath<A> createLineString(String property, Class<? extends A> type);

    <A extends MultiLineString> MultiLineStringPath<A> createMultiLineString(String property, Class<? extends A> type);

    <A extends MultiPoint> MultiPointPath<A> createMultiPoint(String property, Class<? extends A> type);

    <A extends MultiPolygon> MultiPolygonPath<A> createMultiPolygon(String property, Class<? extends A> type);

    <A extends Point> PointPath<A> createPoint(String property, Class<? extends A> type);

    <A extends Polygon> PolygonPath<A> createPolygon(String property, Class<? extends A> type);

    <A extends PolyHedralSurface> PolyhedralSurfacePath<A> createPolyhedralSurface(String property, Class<? extends A> type);

}
