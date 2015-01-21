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
import java.util.List;

import org.geolatte.geom.DimensionalFlag;
import org.geolatte.geom.Geometry;
import org.geolatte.geom.GeometryCollection;
import org.geolatte.geom.GeometryType;
import org.geolatte.geom.LineString;
import org.geolatte.geom.LinearRing;
import org.geolatte.geom.MultiLineString;
import org.geolatte.geom.MultiPoint;
import org.geolatte.geom.MultiPolygon;
import org.geolatte.geom.Point;
import org.geolatte.geom.PointSequence;
import org.geolatte.geom.PointSequenceBuilder;
import org.geolatte.geom.PointSequenceBuilders;
import org.geolatte.geom.Points;
import org.geolatte.geom.Polygon;
import org.geolatte.geom.crs.CrsId;

import com.google.common.collect.Lists;
import com.vividsolutions.jts.io.ByteArrayInStream;
import com.vividsolutions.jts.io.ByteOrderDataInStream;
import com.vividsolutions.jts.io.ByteOrderValues;
import com.vividsolutions.jts.io.InStream;

/**
 * @author tiwe
 *
 */
public class SQLServerGeometryReader {

    private static class Figure {
        int attributes;
        int pointOffset;
    }

    private static class Shape {
        int parentOffset;
        int figureOffset;
        GeometryType type;
    }

    private static GeometryType[] TYPES = new GeometryType[]{
        GeometryType.POINT,
        GeometryType.LINE_STRING,
        GeometryType.POLYGON,
        GeometryType.MULTI_POINT,
        GeometryType.MULTI_LINE_STRING,
        GeometryType.MULTI_POLYGON,
        GeometryType.GEOMETRY_COLLECTION,
        // TODO CircularString
        // TODO CompoundCurve
        // TODO CurvePolygon
        // TODO FullGlobe
    };

    private int srid, version, serializationProps, numberOfPoints;

    private boolean hasZ, hasM, singlePoint, singleLine;

    private DimensionalFlag dimensionalFlag;

    private double[][] points;

    private double[] zValues, mValues;

    private Figure[] figures;

    private Shape[] shapes;

    private CrsId crsId;

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
        //dimensionalFlag = DimensionalFlag.XY;
        dimensionalFlag = DimensionalFlag.d2D;
        if (hasM) {
            if (hasZ) {
                //dimensionalFlag = DimensionalFlag.XYZM;
                dimensionalFlag = DimensionalFlag.d3DM;
            } else {
                //dimensionalFlag = DimensionalFlag.XYM;
                dimensionalFlag = DimensionalFlag.d2DM;
            }
        } else if (hasZ) {
            //dimensionalFlag = DimensionalFlag.XYZ;
            dimensionalFlag = DimensionalFlag.d3D;
        }

        // points
        points = readPoints(dis, numberOfPoints);
        if (hasZ) {
            zValues = readDoubles(dis, numberOfPoints);
        }
        if (hasM) {
            mValues = readDoubles(dis, numberOfPoints);
        }

        crsId = CrsId.valueOf(srid);
        if (singlePoint) {
            return createPoint(0);

        } else if (singleLine) {
            PointSequence points = createPoints(0, 2);
            return new LineString(points);

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
        case LINE_STRING: return decodeLineString(shapeIdx);
        case POLYGON: return decodePolygon(shapeIdx);
        case MULTI_POINT: return decodeMultiPoint(shapeIdx);
        case MULTI_LINE_STRING: return decodeMultiLineString(shapeIdx);
        case MULTI_POLYGON: return decodeMultiPolygon(shapeIdx);
        case GEOMETRY_COLLECTION: return decodeGeometryCollection(shapeIdx);
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
            return Polygon.createEmpty();
        }
        int figureStopIdx = figures.length - 1;
        if (shapeIdx < (shapes.length - 1)) {
            figureStopIdx = shapes[shapeIdx + 1].figureOffset - 1;
        }
        List<LinearRing> linearRings = Lists.newArrayList();
        for (int i = figureOffset; i <= figureStopIdx; i++) {
            linearRings.add(new LinearRing(createPoints(i)));
        }
        return new Polygon(linearRings.toArray(new LinearRing[0]));
    }

    private LineString decodeLineString(int shapeIdx) {
        Shape shape = shapes[shapeIdx];
        return new LineString(createPoints(shape.figureOffset));
    }

    private Point decodePoint(int shapeIdx) {
        int pointIdx = figures[shapes[shapeIdx].figureOffset].pointOffset;
        return createPoint(pointIdx);
    }

    private Point createPoint(int idx) {
        double x = points[idx][0];
        double y = points[idx][1];
        if (hasM) {
            if (hasZ) {
                return Points.create3DM(x, y, zValues[idx], mValues[idx], crsId);
            } else {
                return Points.create2DM(x, y, mValues[idx], crsId);
            }
        } else if (hasZ) {
            return Points.create3D(x, y, zValues[idx], crsId);
        } else {
            return Points.create2D(x, y, crsId);
        }
    }

    private PointSequence createPoints(int idx1, int idx2) {
        PointSequenceBuilder builder = PointSequenceBuilders.fixedSized(idx2 - idx1, dimensionalFlag, crsId);
        for (int i = idx1; i < idx2; i++) {
            builder.add(createPoint(i));
        }
        return builder.toPointSequence();
    }

    private PointSequence createPoints(int figureIdx) {
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
