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

import java.lang.reflect.AnnotatedElement;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

import com.querydsl.spatial.jts.JTSGeometryExpression;
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
public class JTSGeometryPath<T extends Geometry> extends JTSGeometryExpression<T> implements Path<T> {

    private static final long serialVersionUID = 312776751843333543L;

    private final PathImpl<T> pathMixin;

    private volatile JTSGeometryCollectionPath<GeometryCollection> collection;

    private volatile JTSLinearRingPath<LinearRing> linearRing;

    private volatile JTSLineStringPath<LineString> lineString;

    private volatile JTSMultiLineStringPath<MultiLineString> multiLineString;

    private volatile JTSMultiPointPath<MultiPoint> multiPoint;

    private volatile JTSMultiPolygonPath<MultiPolygon> multiPolygon;

    private volatile JTSPointPath<Point> point;

    private volatile JTSPolygonPath<Polygon> polygon;

    public JTSGeometryPath(Path<?> parent, String property) {
        this((Class<? extends T>) Geometry.class, parent, property);
    }

    public JTSGeometryPath(Class<? extends T> type, Path<?> parent, String property) {
        this(type, PathMetadataFactory.forProperty(parent, property));
    }

    public JTSGeometryPath(PathMetadata<?> metadata) {
        this((Class<? extends T>) Geometry.class, metadata);
    }

    public JTSGeometryPath(Class<? extends T> type, PathMetadata<?> metadata) {
        super(new PathImpl<T>(type, metadata));
        this.pathMixin = (PathImpl<T>)mixin;
    }

    public JTSGeometryPath(String var) {
        this((Class<? extends T>) Geometry.class, PathMetadataFactory.forVariable(var));
    }

    public JTSGeometryCollectionPath<GeometryCollection> asCollection() {
        if (collection == null) {
            collection = new JTSGeometryCollectionPath<GeometryCollection>(pathMixin.getMetadata());
        }
        return collection;
    }

    public JTSLinearRingPath<LinearRing> asLinearRing() {
        if (linearRing == null) {
            linearRing = new JTSLinearRingPath<LinearRing>(pathMixin.getMetadata());
        }
        return linearRing;
    }

    public JTSLineStringPath<LineString> asLineString() {
        if (lineString == null) {
            lineString = new JTSLineStringPath<LineString>(pathMixin.getMetadata());
        }
        return lineString;
    }

    public JTSMultiLineStringPath<MultiLineString> asMultiLineString() {
        if (multiLineString == null) {
            multiLineString = new JTSMultiLineStringPath<MultiLineString>(pathMixin.getMetadata());
        }
        return multiLineString;
    }

    public JTSMultiPointPath<MultiPoint> asMultiPoint() {
        if (multiPoint == null) {
            multiPoint = new JTSMultiPointPath<MultiPoint>(pathMixin.getMetadata());
        }
        return multiPoint;
    }

    public JTSMultiPolygonPath<MultiPolygon> asMultiPolygon() {
        if (multiPolygon == null) {
            multiPolygon = new JTSMultiPolygonPath<MultiPolygon>(pathMixin.getMetadata());
        }
        return multiPolygon;
    }

    public JTSPointPath<Point> asPoint() {
        if (point == null) {
            point = new JTSPointPath<Point>(pathMixin.getMetadata());
        }
        return point;
    }

    public JTSPolygonPath<Polygon> asPolygon() {
        if (polygon == null) {
            polygon = new JTSPolygonPath<Polygon>(pathMixin.getMetadata());
        }
        return polygon;
    }

    @Override
    public final <R,C> R accept(Visitor<R,C> v, C context) {
        return v.visit(pathMixin, context);
    }

    public JTSGeometryPath(Class<? extends T> type, String var) {
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
