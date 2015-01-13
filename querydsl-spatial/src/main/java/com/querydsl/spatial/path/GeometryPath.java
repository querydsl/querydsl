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

import java.lang.reflect.AnnotatedElement;

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

import com.querydsl.spatial.GeometryExpression;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathImpl;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.PathMetadataFactory;
import com.querydsl.core.types.Visitor;

/**
 * @author tiwe
 *
 * @param <T>
 */
public class GeometryPath<T extends Geometry> extends GeometryExpression<T> implements Path<T> {

    private static final long serialVersionUID = 312776751843333543L;

    private final PathImpl<T> pathMixin;

    private volatile GeometryCollectionPath<GeometryCollection> collection;

    private volatile LinearRingPath<LinearRing> linearRing;

    private volatile LineStringPath<LineString> lineString;

    private volatile MultiLineStringPath<MultiLineString> multiLineString;

    private volatile MultiPointPath<MultiPoint> multiPoint;

    private volatile MultiPolygonPath<MultiPolygon> multiPolygon;

    private volatile PointPath<Point> point;

    private volatile PolygonPath<Polygon> polygon;

    private volatile PolyhedralSurfacePath<PolyHedralSurface> polyhedralSurface;

    public GeometryPath(Path<?> parent, String property) {
        this((Class<? extends T>) Geometry.class, parent, property);
    }

    public GeometryPath(Class<? extends T> type, Path<?> parent, String property) {
        this(type, PathMetadataFactory.forProperty(parent, property));
    }

    public GeometryPath(PathMetadata<?> metadata) {
        this((Class<? extends T>) Geometry.class, metadata);
    }

    public GeometryPath(Class<? extends T> type, PathMetadata<?> metadata) {
        super(new PathImpl<T>(type, metadata));
        this.pathMixin = (PathImpl<T>)mixin;
    }

    public GeometryPath(String var) {
        this((Class<? extends T>) Geometry.class, PathMetadataFactory.forVariable(var));
    }

    public GeometryCollectionPath<GeometryCollection> asCollection() {
        if (collection == null) {
            collection = new GeometryCollectionPath<GeometryCollection>(pathMixin.getMetadata());
        }
        return collection;
    }

    public LinearRingPath<LinearRing> asLinearRing() {
        if (linearRing == null) {
            linearRing = new LinearRingPath<LinearRing>(pathMixin.getMetadata());
        }
        return linearRing;
    }

    public LineStringPath<LineString> asLineString() {
        if (lineString == null) {
            lineString = new LineStringPath<LineString>(pathMixin.getMetadata());
        }
        return lineString;
    }

    public MultiLineStringPath<MultiLineString> asMultiLineString() {
        if (multiLineString == null) {
            multiLineString = new MultiLineStringPath<MultiLineString>(pathMixin.getMetadata());
        }
        return multiLineString;
    }

    public MultiPointPath<MultiPoint> asMultiPoint() {
        if (multiPoint == null) {
            multiPoint = new MultiPointPath<MultiPoint>(pathMixin.getMetadata());
        }
        return multiPoint;
    }

    public MultiPolygonPath<MultiPolygon> asMultiPolygon() {
        if (multiPolygon == null) {
            multiPolygon = new MultiPolygonPath<MultiPolygon>(pathMixin.getMetadata());
        }
        return multiPolygon;
    }

    public PointPath<Point> asPoint() {
        if (point == null) {
            point = new PointPath<Point>(pathMixin.getMetadata());
        }
        return point;
    }

    public PolygonPath<Polygon> asPolygon() {
        if (polygon == null) {
            polygon = new PolygonPath<Polygon>(pathMixin.getMetadata());
        }
        return polygon;
    }

    public PolyhedralSurfacePath<PolyHedralSurface> asPolygHedralSurface() {
        if (polyhedralSurface == null) {
            polyhedralSurface = new PolyhedralSurfacePath<PolyHedralSurface>(pathMixin.getMetadata());
        }
        return polyhedralSurface;
    }

    @Override
    public final <R,C> R accept(Visitor<R,C> v, C context) {
        return v.visit(pathMixin, context);
    }

    public GeometryPath(Class<? extends T> type, String var) {
        this(type, PathMetadataFactory.forVariable(var));
    }

    @Override
    public PathMetadata<?> getMetadata() {
        return pathMixin.getMetadata();
    }

    @Override
    public Path<?> getRoot() {
        return pathMixin.getRoot();
    }

    @Override
    public AnnotatedElement getAnnotatedElement() {
        return pathMixin.getAnnotatedElement();
    }

}
