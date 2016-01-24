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
package com.querydsl.spatial;

import java.lang.reflect.AnnotatedElement;

import org.geolatte.geom.*;

import com.querydsl.core.types.*;

/**
 * {@code GeometryPath} extends {@link GeometryExpression} to implement the
 * {@link Path} interface
 *
 * @author tiwe
 *
 * @param <T>
 */
public class GeometryPath<T extends Geometry> extends GeometryExpression<T> implements Path<T> {

    private static final long serialVersionUID = 312776751843333543L;

    private final PathImpl<T> pathMixin;

    private transient volatile GeometryCollectionPath<GeometryCollection> collection;

    private transient volatile LinearRingPath<LinearRing> linearRing;

    private transient volatile LineStringPath<LineString> lineString;

    private transient volatile MultiLineStringPath<MultiLineString> multiLineString;

    private transient volatile MultiPointPath<MultiPoint> multiPoint;

    private transient volatile MultiPolygonPath<MultiPolygon> multiPolygon;

    private transient volatile PointPath<Point> point;

    private transient volatile PolygonPath<Polygon> polygon;

    private transient volatile PolyhedralSurfacePath<PolyHedralSurface> polyhedralSurface;

    protected GeometryPath(PathImpl<T> mixin) {
        super(mixin);
        this.pathMixin = mixin;
    }

    @SuppressWarnings("unchecked")
    public GeometryPath(Path<?> parent, String property) {
        this((Class<? extends T>) Geometry.class, parent, property);
    }

    public GeometryPath(Class<? extends T> type, Path<?> parent, String property) {
        this(type, PathMetadataFactory.forProperty(parent, property));
    }

    @SuppressWarnings("unchecked")
    public GeometryPath(PathMetadata metadata) {
        this((Class<? extends T>) Geometry.class, metadata);
    }

    public GeometryPath(Class<? extends T> type, PathMetadata metadata) {
        super(ExpressionUtils.path(type, metadata));
        this.pathMixin = (PathImpl<T>) mixin;
    }

    @SuppressWarnings("unchecked")
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

    public PolyhedralSurfacePath<PolyHedralSurface> asPolyHedralSurface() {
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
