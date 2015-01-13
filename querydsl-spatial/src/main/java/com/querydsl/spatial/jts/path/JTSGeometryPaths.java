/*
 * Copyright 2014, Timo Westkämper
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
package com.querydsl.spatial.jts.path;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

/**
 * @author tiwe
 *
 */
public interface JTSGeometryPaths {

    <A extends GeometryCollection> JTSGeometryCollectionPath<A> createGeometryCollection(String property, Class<? extends A> type);

    <A extends Geometry> JTSGeometryPath<A> createGeometry(String property, Class<? extends A> type);

    <A extends LinearRing> JTSLinearRingPath<A> createLinearRing(String property, Class<? extends A> type);

    <A extends LineString> JTSLineStringPath<A> createLineString(String property, Class<? extends A> type);

    <A extends MultiLineString> JTSMultiLineStringPath<A> createMultiLineString(String property, Class<? extends A> type);

    <A extends MultiPoint> JTSMultiPointPath<A> createMultiPoint(String property, Class<? extends A> type);

    <A extends MultiPolygon> JTSMultiPolygonPath<A> createMultiPolygon(String property, Class<? extends A> type);

    <A extends Point> JTSPointPath<A> createPoint(String property, Class<? extends A> type);

    <A extends Polygon> JTSPolygonPath<A> createPolygon(String property, Class<? extends A> type);

}
