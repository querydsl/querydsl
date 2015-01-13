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

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;

import org.geolatte.geom.Geometry;
import org.geolatte.geom.GeometryCollection;
import org.geolatte.geom.LineString;
import org.geolatte.geom.MultiLineString;
import org.geolatte.geom.MultiPoint;
import org.geolatte.geom.MultiPolygon;
import org.geolatte.geom.Point;
import org.geolatte.geom.PolyHedralSurface;
import org.geolatte.geom.Polygon;

import com.google.common.collect.Lists;

/**
 * @author tiwe
 *
 */
public class SQLServerGeometryWriter {

    private static final int POINT = 1;

    private static final int LINE_STRING = 2;

    private static final int POLYGON = 3;

    private static final int MULTI_POINT = 4;

    private static final int MULTI_LINESTRING = 5;

    private static final int MULTI_POLYGON = 6;

    private static final int GEOMETRY_COLLECTION = 7;

    private static class Figure {
        int attributes;
        int pointOffset;
    }

    private static class Shape {
        int parentOffset;
        int figureOffset;
        int type;
    }

    private List<Point> points = Lists.newArrayList();

    private List<Figure> figures = Lists.newArrayList();

    private List<Shape> shapes = Lists.newArrayList();

    private int calculateCapacity(Geometry geometry) {
        int numPoints = points.size();
        int prefixSize = 6;

        if (geometry instanceof Point) {
            int capacity = prefixSize + 16 * numPoints;
            if (geometry.is3D()) {
                capacity += 8 * numPoints;
            }
            if (geometry.isMeasured()) {
                capacity += 8 * numPoints;
            }
            return capacity;
        }

        int pointSize = 16 + (geometry.is3D() ? 8 : 0) + (geometry.isMeasured() ? 8 : 0);
        int size = prefixSize + 3 * 4; // prefix + 3 ints for points, shapes and
                                       // figures
        size += numPoints * pointSize;
        size += figures.size() * 5;
        size += shapes.size() * 9;
        return size;
    }

    public byte[] write(Geometry geometry) throws IOException {
        visit(geometry, -1);

        int bytes = calculateCapacity(geometry);
        boolean singlePoint = geometry instanceof Point;
        ByteBuffer buffer = ByteBuffer.allocate(bytes);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(geometry.getSRID());
        buffer.put((byte)1);
        buffer.put((byte)((geometry.is3D() ? 1 : 0)
                 + (geometry.isMeasured() ? 2 : 0)
                 + 4 // is valid
                 + (singlePoint ? 8 : 0)
                 + 0)); // TODO

        if (!singlePoint) {
            buffer.putInt(points.size());
        }
        for (Point point : points) {
            buffer.putDouble(point.getX());
            buffer.putDouble(point.getY());
        }
        if (geometry.is3D()) {
            for (Point point : points) {
                buffer.putDouble(point.getZ());
            }
        }
        if (geometry.isMeasured()) {
            for (Point point : points) {
                buffer.putDouble(point.getM());
            }
        }
        if (!singlePoint) {
            buffer.putInt(figures.size());
            for (Figure figure : figures) {
                buffer.put((byte)figure.attributes);
                buffer.putInt(figure.pointOffset);
            }
            buffer.putInt(shapes.size());
            for (Shape shape : shapes) {
                buffer.putInt(shape.parentOffset);
                buffer.putInt(shape.figureOffset);
                buffer.put((byte)shape.type);
            }
        }
        return buffer.array();
    }

    private void visit(Geometry geometry, int parent) {
        switch (geometry.getGeometryType()) {
        case POINT: visit((Point)geometry, parent); break;
        case POLYGON: visit((Polygon)geometry, parent); break;
        case LINE_STRING: visit((LineString)geometry, parent); break;
        case MULTI_POINT: visit((MultiPoint)geometry, parent); break;
        case MULTI_LINE_STRING: visit((MultiLineString)geometry, parent); break;
        case MULTI_POLYGON: visit((MultiPolygon)geometry, parent); break;
        case GEOMETRY_COLLECTION: visit((GeometryCollection)geometry, parent); break;
        default: throw new IllegalArgumentException(geometry.toString());
        }
    }

    public void visit(Point point, int parent) {
        Shape shape = new Shape();
        shape.type = POINT;
        shape.figureOffset = figures.size();
        shape.parentOffset = parent;
        shapes.add(shape);

        Figure figure = new Figure();
        figure.attributes = 1;
        figure.pointOffset = points.size();
        figures.add(figure);

        points.add(point);
    }

    public void visit(LineString lineString, int parent) {
        Shape shape = new Shape();
        shape.type = LINE_STRING;
        shape.figureOffset = figures.size();
        shape.parentOffset = parent;
        shapes.add(shape);

        Figure figure = new Figure();
        figure.attributes = 1;
        figure.pointOffset = points.size();
        figures.add(figure);

        for (Point point : lineString.getPoints()) {
            points.add(point);
        }
    }

    public void visit(Polygon polygon, int parent) {
        Shape shape = new Shape();
        shape.type = POLYGON;
        shape.figureOffset = figures.size();
        shape.parentOffset = parent;
        shapes.add(shape);

        // exterior
        Figure figure = new Figure();
        figure.attributes = 2;
        figure.pointOffset = points.size();
        figures.add(figure);
        for (Point point : polygon.getExteriorRing().getPoints()) {
            points.add(point);
        }

        // interior
        for (int i = 0; i < polygon.getNumInteriorRing(); i++) {
            figure = new Figure();
            figure.attributes = 0;
            figure.pointOffset = points.size();
            figures.add(figure);
            for (Point point : polygon.getInteriorRingN(i).getPoints()) {
                points.add(point);
            }
        }
    }

    public void visit(MultiPoint collection, int parent) {
        Shape shape = new Shape();
        shape.type = MULTI_POINT;
        shape.figureOffset = figures.size();
        shape.parentOffset = parent;
        shapes.add(shape);

        parent = shapes.size() - 1;
        for (int i = 0; i < collection.getNumGeometries(); i++) {
            visit(collection.getGeometryN(i), parent);
        }
    }

    public void visit(MultiLineString collection, int parent) {
        Shape shape = new Shape();
        shape.type = MULTI_LINESTRING;
        shape.figureOffset = figures.size();
        shape.parentOffset = parent;
        shapes.add(shape);

        parent = shapes.size() - 1;
        for (int i = 0; i < collection.getNumGeometries(); i++) {
            visit(collection.getGeometryN(i), parent);
        }
    }

    public void visit(MultiPolygon collection, int parent) {
        Shape shape = new Shape();
        shape.type = MULTI_POLYGON;
        shape.figureOffset = figures.size();
        shape.parentOffset = parent;
        shapes.add(shape);

        parent = shapes.size() - 1;
        for (int i = 0; i < collection.getNumGeometries(); i++) {
            visit(collection.getGeometryN(i), parent);
        }
    }

    public void visit(GeometryCollection collection, int parent) {
        Shape shape = new Shape();
        shape.type = GEOMETRY_COLLECTION;
        shape.figureOffset = figures.size();
        shape.parentOffset = parent;
        shapes.add(shape);

        parent = shapes.size() - 1;
        for (int i = 0; i < collection.getNumGeometries(); i++) {
            visit(collection.getGeometryN(i), parent);
        }
    }

    public void visit(PolyHedralSurface surface, int parent) {
        Shape shape = new Shape();
        shape.type = -1; // TODO
        shape.figureOffset = figures.size();
        shape.parentOffset = parent;
        shapes.add(shape);

        parent = shapes.size() - 1;
        for (int i = 0; i < surface.getNumPatches(); i++) {
            visit(surface.getPatchN(i), parent);
        }
    }

}
