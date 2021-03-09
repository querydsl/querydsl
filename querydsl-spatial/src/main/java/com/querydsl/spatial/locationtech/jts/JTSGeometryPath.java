/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
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
package com.querydsl.spatial.locationtech.jts;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathImpl;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.PathMetadataFactory;
import com.querydsl.core.types.Visitor;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

import java.lang.reflect.AnnotatedElement;

/**
 * {@code JTSGeometryPath} extends {@link JTSGeometryExpression} to implement the
 * {@link Path} interface
 *
 * @author tiwe
 *
 * @param <T>
 */
public class JTSGeometryPath<T extends Geometry> extends JTSGeometryExpression<T> implements Path<T> {

    private static final long serialVersionUID = 312776751843333543L;

    private final PathImpl<T> pathMixin;

    private transient volatile JTSGeometryCollectionPath<GeometryCollection> collection;

    private transient volatile JTSLinearRingPath<LinearRing> linearRing;

    private transient volatile JTSLineStringPath<LineString> lineString;

    private transient volatile JTSMultiLineStringPath<MultiLineString> multiLineString;

    private transient volatile JTSMultiPointPath<MultiPoint> multiPoint;

    private transient volatile JTSMultiPolygonPath<MultiPolygon> multiPolygon;

    private transient volatile JTSPointPath<Point> point;

    private transient volatile JTSPolygonPath<Polygon> polygon;

    @SuppressWarnings("unchecked")
    public JTSGeometryPath(Path<?> parent, String property) {
        this((Class<? extends T>) Geometry.class, parent, property);
    }

    public JTSGeometryPath(Class<? extends T> type, Path<?> parent, String property) {
        this(type, PathMetadataFactory.forProperty(parent, property));
    }

    @SuppressWarnings("unchecked")
    public JTSGeometryPath(PathMetadata metadata) {
        this((Class<? extends T>) Geometry.class, metadata);
    }

    public JTSGeometryPath(Class<? extends T> type, PathMetadata metadata) {
        super(ExpressionUtils.path(type, metadata));
        this.pathMixin = (PathImpl<T>) mixin;
    }

    @SuppressWarnings("unchecked")
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
    public PathMetadata getMetadata() {
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
