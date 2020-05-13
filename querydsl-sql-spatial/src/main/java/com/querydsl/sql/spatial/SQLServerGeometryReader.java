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
package com.querydsl.sql.spatial;

import java.io.IOException;
import java.util.List;

import org.geolatte.geom.*;
import org.geolatte.geom.crs.CoordinateReferenceSystem;
import org.geolatte.geom.crs.CoordinateReferenceSystems;
import org.geolatte.geom.crs.CrsRegistry;

import com.google.common.collect.Lists;
import org.locationtech.jts.io.ByteArrayInStream;
import org.locationtech.jts.io.ByteOrderDataInStream;
import org.locationtech.jts.io.ByteOrderValues;
import org.locationtech.jts.io.InStream;

class SQLServerGeometryReader {

    private static class Figure {
        int attributes;
        int pointOffset;
    }

    private static class Shape {
        int parentOffset;
        int figureOffset;
        GeometryType type;
    }

    private static final GeometryType[] TYPES = new GeometryType[]{
        GeometryType.POINT,
        GeometryType.LINESTRING,
        GeometryType.POLYGON,
        GeometryType.MULTIPOINT,
        GeometryType.MULTILINESTRING,
        GeometryType.MULTIPOLYGON,
        GeometryType.GEOMETRYCOLLECTION,
        // TODO CircularString
        // TODO CompoundCurve
        // TODO CurvePolygon
        // TODO FullGlobe
    };

    private int srid, version, serializationProps, numberOfPoints;

    private boolean hasZ, hasM, singlePoint, singleLine;

    private double[][] points;

    private double[] zValues, mValues;

    private Figure[] figures;

    private Shape[] shapes;

    private CoordinateReferenceSystem crs;

    public Geometry read(byte[] bytes) throws IOException {
        return read(new ByteArrayInStream(bytes));
    }

    public Geometry read(InStream is) throws IOException {
        ByteOrderDataInStream dis = new ByteOrderDataInStream(is);
        dis.setOrder(ByteOrderValues.LITTLE_ENDIAN);

        srid = dis.readInt();
        version = dis.readByte();
        serializationProps = dis.readByte();
        hasZ = (serializationProps & 1) == 1;
        hasM = (serializationProps & 2) == 2;
        singlePoint = (serializationProps & 8) == 8;
        singleLine = (serializationProps & 16) == 16;
        numberOfPoints = 1;
        if (singleLine) {
            numberOfPoints = 2;
        } else if (!singlePoint) {
            numberOfPoints = dis.readInt();
        }

        // points
        points = readPoints(dis, numberOfPoints);
        if (hasZ) {
            zValues = readDoubles(dis, numberOfPoints);
        }
        if (hasM) {
            mValues = readDoubles(dis, numberOfPoints);
        }

        if (CrsRegistry.hasCoordinateReferenceSystemForEPSG(srid)) {
            crs = CrsRegistry.getCoordinateReferenceSystemForEPSG(srid, null);
        } else if (hasM) {
            if (hasZ) {
                crs = CoordinateReferenceSystems.PROJECTED_3DM_METER;
            } else {
                crs = CoordinateReferenceSystems.PROJECTED_2DM_METER;
            }
        } else if (hasZ) {
            crs = CoordinateReferenceSystems.PROJECTED_3D_METER;
        } else {
            crs = CoordinateReferenceSystems.PROJECTED_2D_METER;
        }

        if (singlePoint) {
            return createPoint(0);

        } else if (singleLine) {
            PositionSequence points = createPoints(0, 2);
            return new LineString(points, crs);

        } else {
            // figures
            int numberOfFigures = dis.readInt();
            figures = readFigures(dis, numberOfFigures);
            // shapes
            int numberOfShapes = dis.readInt();
            shapes = readShapes(dis, numberOfShapes);
            return decode(0);
        }
    }

    private Geometry decode(int shapeIdx) {
        switch (shapes[shapeIdx].type) {
        case POINT: return decodePoint(shapeIdx);
        case LINESTRING: return decodeLineString(shapeIdx);
        case POLYGON: return decodePolygon(shapeIdx);
        case MULTIPOINT: return decodeMultiPoint(shapeIdx);
        case MULTILINESTRING: return decodeMultiLineString(shapeIdx);
        case MULTIPOLYGON: return decodeMultiPolygon(shapeIdx);
        case GEOMETRYCOLLECTION: return decodeGeometryCollection(shapeIdx);
        default: throw new IllegalArgumentException(String.valueOf(shapeIdx));
        }
    }

    private GeometryCollection decodeGeometryCollection(int shapeIdx) {
        List<Geometry> geometries = Lists.newArrayList();
        for (int i = shapeIdx; i < shapes.length; i++) {
            if (shapes[i].parentOffset == shapeIdx) {
                geometries.add(decode(i));
            }
        }
        return new GeometryCollection(geometries.toArray(new Geometry[0]));
    }

    private MultiLineString decodeMultiLineString(int shapeIdx) {
        List<LineString> lineStrings = Lists.newArrayList();
        for (int i = shapeIdx; i < shapes.length; i++) {
            if (shapes[i].parentOffset == shapeIdx) {
                lineStrings.add(decodeLineString(i));
            }
        }
        return new MultiLineString(lineStrings.toArray(new LineString[0]));
    }

    private MultiPolygon decodeMultiPolygon(int shapeIdx) {
        List<Polygon> polygons = Lists.newArrayList();
        for (int i = shapeIdx; i < shapes.length; i++) {
            if (shapes[i].parentOffset == shapeIdx) {
                polygons.add(decodePolygon(i));
            }
        }
        return new MultiPolygon(polygons.toArray(new Polygon[0]));
    }

    private MultiPoint decodeMultiPoint(int shapeIdx) {
        List<Point> points = Lists.newArrayList();
        for (int i = shapeIdx; i < shapes.length; i++) {
            if (shapes[i].parentOffset == shapeIdx) {
                points.add(decodePoint(i));
            }
        }
        return new MultiPoint(points.toArray(new Point[0]));
    }

    private Polygon decodePolygon(int shapeIdx) {
        Shape shape = shapes[shapeIdx];
        int figureOffset = shape.figureOffset;
        if (figureOffset <= -1) {
            return Geometries.mkEmptyPolygon(crs);
        }
        int figureStopIdx = figures.length - 1;
        if (shapeIdx < (shapes.length - 1)) {
            figureStopIdx = shapes[shapeIdx + 1].figureOffset - 1;
        }
        List<LinearRing> linearRings = Lists.newArrayList();
        for (int i = figureOffset; i <= figureStopIdx; i++) {
            linearRings.add(new LinearRing(createPoints(i), crs));
        }
        return new Polygon(linearRings.toArray(new LinearRing[0]));
    }

    private LineString decodeLineString(int shapeIdx) {
        Shape shape = shapes[shapeIdx];
        return new LineString(createPoints(shape.figureOffset), crs);
    }

    private Point decodePoint(int shapeIdx) {
        int pointIdx = figures[shapes[shapeIdx].figureOffset].pointOffset;
        return createPoint(pointIdx);
    }

    private Point createPoint(int idx) {
        return new Point(createPosition(idx), crs);
    }

    private Position createPosition(int idx) {
        double x = points[idx][0];
        double y = points[idx][1];
        if (hasM) {
            if (hasZ) {
                return new C3DM(x, y, zValues[idx], mValues[idx]);
            } else {
                return new C2DM(x, y, mValues[idx]);
            }
        } else if (hasZ) {
            return new C3D(x, y, zValues[idx]);
        } else {
            return new C2D(x, y);
        }
    }

    private PositionSequence createPoints(int idx1, int idx2) {
        final PositionSequenceBuilder builder =
                PositionSequenceBuilders.fixedSized(idx2 - idx1, crs.getPositionClass());

        for (int i = idx1; i < idx2; i++) {
            builder.add(createPosition(i));
        }
        return builder.toPositionSequence();
    }

    private PositionSequence createPoints(int figureIdx) {
        int idx1 = figures[figureIdx].pointOffset;
        int idx2 = points.length;
        if (figureIdx < (figures.length - 1)) {
            idx2 = figures[figureIdx + 1].pointOffset;
        }
        return createPoints(idx1, idx2);
    }

    private double[] readDoubles(ByteOrderDataInStream is, int num) throws IOException {
        double[] doubles = new double[num];
        for (int i = 0; i < num; i++) {
            doubles[i] = is.readDouble();
        }
        return doubles;
    }

    private Figure[] readFigures(ByteOrderDataInStream is, int num) throws IOException {
        Figure[] figures = new Figure[num];
        for (int i = 0; i < num; i++) {
            Figure figure = new Figure();
            figure.attributes = is.readByte();
            figure.pointOffset = is.readInt();
            figures[i] = figure;
        }
        return figures;
    }

    private double[][] readPoints(ByteOrderDataInStream is, int num) throws IOException {
        double[][] points = new double[num][];
        for (int i = 0; i < num; i++) {
            points[i] = new double[]{is.readDouble(), is.readDouble()};
        }
        return points;
    }

    private Shape[] readShapes(ByteOrderDataInStream is, int num) throws IOException {
        Shape[] shapes = new Shape[num];
        for (int i = 0; i < num; i++) {
            Shape shape = new Shape();
            shape.parentOffset = is.readInt();
            shape.figureOffset = is.readInt();
            shape.type = TYPES[is.readByte() - 1];
            shapes[i] = shape;
        }
        return shapes;
    }


}
