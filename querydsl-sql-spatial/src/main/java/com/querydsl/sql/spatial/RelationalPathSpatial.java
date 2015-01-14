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

import com.querydsl.spatial.path.GeometryCollectionPath;
import com.querydsl.spatial.path.GeometryPath;
import com.querydsl.spatial.path.GeometryPaths;
import com.querydsl.spatial.path.LineStringPath;
import com.querydsl.spatial.path.LinearRingPath;
import com.querydsl.spatial.path.MultiLineStringPath;
import com.querydsl.spatial.path.MultiPointPath;
import com.querydsl.spatial.path.MultiPolygonPath;
import com.querydsl.spatial.path.PointPath;
import com.querydsl.spatial.path.PolygonPath;
import com.querydsl.spatial.path.PolyhedralSurfacePath;
import com.querydsl.sql.RelationalPathBase;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.PathMetadataFactory;

/**
 * @author tiwe
 *
 * @param <T>
 */
public class RelationalPathSpatial<T> extends RelationalPathBase<T> implements GeometryPaths {

    private static final long serialVersionUID = -7176236689794620277L;

    public RelationalPathSpatial(Class<? extends T> type, String variable, String schema, String table) {
        this(type, PathMetadataFactory.forVariable(variable), schema, table);
    }

    public RelationalPathSpatial(Class<? extends T> type, PathMetadata<?> metadata, String schema, String table) {
        super(type, metadata, schema, table);
    }

    @Override
    public <A extends GeometryCollection> GeometryCollectionPath<A> createGeometryCollection(
            String property, Class<? extends A> type) {
        return add(new GeometryCollectionPath<A>(type, forProperty(property)));
    }

    @Override
    public <A extends Geometry> GeometryPath<A> createGeometry(String property,
            Class<? extends A> type) {
        return add(new GeometryPath<A>(type, forProperty(property)));
    }

    @Override
    public <A extends LinearRing> LinearRingPath<A> createLinearRing(String property,
            Class<? extends A> type) {
        return add(new LinearRingPath<A>(type, forProperty(property)));
    }

    @Override
    public <A extends LineString> LineStringPath<A> createLineString(String property,
            Class<? extends A> type) {
        return add(new LineStringPath<A>(type, forProperty(property)));
    }

    @Override
    public <A extends MultiLineString> MultiLineStringPath<A> createMultiLineString(
            String property, Class<? extends A> type) {
        return add(new MultiLineStringPath<A>(type, forProperty(property)));
    }

    @Override
    public <A extends MultiPoint> MultiPointPath<A> createMultiPoint(String property,
            Class<? extends A> type) {
        return add(new MultiPointPath<A>(type, forProperty(property)));
    }

    @Override
    public <A extends MultiPolygon> MultiPolygonPath<A> createMultiPolygon(String property,
            Class<? extends A> type) {
        return add(new MultiPolygonPath<A>(type, forProperty(property)));
    }

    @Override
    public <A extends Point> PointPath<A> createPoint(String property, Class<? extends A> type) {
        return add(new PointPath<A>(type, forProperty(property)));
    }

    @Override
    public <A extends Polygon> PolygonPath<A> createPolygon(String property, Class<? extends A> type) {
        return add(new PolygonPath<A>(type, forProperty(property)));
    }

    @Override
    public <A extends PolyHedralSurface> PolyhedralSurfacePath<A> createPolyhedralSurface(
            String property, Class<? extends A> type) {
        return add(new PolyhedralSurfacePath<A>(type, forProperty(property)));
    }

}
