/*
 * Copyright 2020, The Querydsl Team (http://www.querydsl.com/team)
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
package com.querydsl.sql.spatial2;

import com.google.common.collect.Lists;
import org.geolatte.geom.*;
import org.geolatte.geom.crs.CoordinateReferenceSystems;
import org.locationtech.jts.io.ByteArrayInStream;
import org.locationtech.jts.io.ByteOrderDataInStream;
import org.locationtech.jts.io.ByteOrderValues;
import org.locationtech.jts.io.InStream;

import java.io.IOException;
import java.util.List;

import static org.geolatte.geom.builder.DSL.*;
import static org.geolatte.geom.builder.DSL.linestring;

/**
 * Provides a way of reading.
 *
 * @author Timo Westkämper
 * @author Nikita Kochkurov (kochkurovn@gmail.com)
 */
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
    };

    private int srid, serializationProps, numberOfPoints;

    private boolean hasZ, hasM, singlePoint, singleLine;

    private double[][] points;

    private double[] zValues, mValues;

    private Figure[] figures;

    private Shape[] shapes;

    private Position dimension;

    public Geometry<?> read(byte[] bytes) throws IOException {
        return read(new ByteArrayInStream(bytes));
    }

    public Geometry<?> read(InStream is) throws IOException {
        ByteOrderDataInStream dis = new ByteOrderDataInStream(is);
        dis.setOrder(ByteOrderValues.LITTLE_ENDIAN);

        srid = dis.readInt();
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
        dimension = new C2D();
        if (hasM) {
            if (hasZ) {
                dimension = new C3DM();
            } else {
                dimension = new C2DM();
            }
        } else if (hasZ) {
            dimension = new C3D();
        }

        // points
        points = readPoints(dis, numberOfPoints);
        if (hasZ) {
            zValues = readDoubles(dis, numberOfPoints);
        }
        if (hasM) {
            mValues = readDoubles(dis, numberOfPoints);
        }

        if (singlePoint) {
            return createPoint(0);

        } else if (singleLine) {
            PositionSequence<?> positionSequence = createPoints(0, 2, dimension);
            if (positionSequence.getPositionClass().equals(C2D.class)) {
                C2D[] positions = new C2D[positionSequence.size()];
                for (int i = 0; i < positionSequence.size(); i++) {
                    positions[i] = c(positionSequence.getPositionN(i).getCoordinate(0), positionSequence.getPositionN(i).getCoordinate(1));
                }
                return linestring(CoordinateReferenceSystems.PROJECTED_2D_METER, positions);
            } else if (positionSequence.getPositionClass().equals(C2DM.class)) {
                C2DM[] positions = new C2DM[positionSequence.size()];
                for (int i = 0; i < positionSequence.size(); i++) {
                    positions[i] = cM(positionSequence.getPositionN(i).getCoordinate(0),
                            positionSequence.getPositionN(i).getCoordinate(1),
                            positionSequence.getPositionN(i).getCoordinate(2));
                }
                return linestring(CoordinateReferenceSystems.PROJECTED_2DM_METER, positions);
            } else if (positionSequence.getPositionClass().equals(C3D.class)) {
                C3D[] positions = new C3D[positionSequence.size()];
                for (int i = 0; i < positionSequence.size(); i++) {
                    positions[i] = c(positionSequence.getPositionN(i).getCoordinate(0),
                            positionSequence.getPositionN(i).getCoordinate(1),
                            positionSequence.getPositionN(i).getCoordinate(2));
                }
                return linestring(CoordinateReferenceSystems.PROJECTED_3D_METER, positions);
            } else if (positionSequence.getPositionClass().equals(C3DM.class)) {
                C3DM[] positions = new C3DM[positionSequence.size()];
                for (int i = 0; i < positionSequence.size(); i++) {
                    positions[i] = c(positionSequence.getPositionN(i).getCoordinate(0),
                            positionSequence.getPositionN(i).getCoordinate(1),
                            positionSequence.getPositionN(i).getCoordinate(2),
                            positionSequence.getPositionN(i).getCoordinate(3));
                }
                return linestring(CoordinateReferenceSystems.PROJECTED_3DM_METER, positions);
            } else {
                return linestring(CoordinateReferenceSystems.PROJECTED_2D_METER);
            }
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

    private Geometry<?> decode(int shapeIdx) {
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

    private GeometryCollection<?> decodeGeometryCollection(int shapeIdx) {
        List<Geometry<?>> geometries = Lists.newArrayList();
        for (int i = shapeIdx; i < shapes.length; i++) {
            if (shapes[i].parentOffset == shapeIdx) {
                geometries.add(decode(i));
            }
        }
        Geometry<?> first = geometries.get(0);
        if (first instanceof Point) {
            return new GeometryCollection<Position>((Point.class.cast(new Geometry[0])));
        } else if (first instanceof LinearRing) {
            return new GeometryCollection<Position>((LinearRing.class.cast(new Geometry[0])));
        } else if (first instanceof LineString) {
            return new GeometryCollection<Position>((LineString.class.cast(new Geometry[0])));
        } else if (first instanceof MultiLineString) {
            return new GeometryCollection<Position>((MultiLineString.class.cast(new Geometry[0])));
        } else if (first instanceof Polygon) {
            return new GeometryCollection<Position>((Polygon.class.cast(new Geometry[0])));
        } else if (first instanceof MultiPoint) {
            return new GeometryCollection<Position>((MultiPoint.class.cast(new Geometry[0])));
        } else if (first instanceof MultiPolygon) {
            return new GeometryCollection<Position>((MultiPolygon.class.cast(new Geometry[0])));
        } else if (first instanceof GeometryCollection) {
            return new GeometryCollection<Position>((GeometryCollection.class.cast(new Geometry[0])));
        } else {
            throw new IllegalArgumentException(Geometry[].class.getName());
        }
    }

    private MultiLineString<?> decodeMultiLineString(int shapeIdx) {
        List<LineString<?>> lineStrings = Lists.newArrayList();
        for (int i = shapeIdx; i < shapes.length; i++) {
            if (shapes[i].parentOffset == shapeIdx) {
                lineStrings.add(decodeLineString(i));
            }
        }
        LineString<?> first = lineStrings.get(0);
        if (first.getPositionClass().equals(C2D.class)) {
            return new MultiLineString<C2D>(LinearRing.class.cast(lineStrings.toArray(new LineString[0])));
        } else if (first.getPositionClass().equals(C2DM.class)) {
            return new MultiLineString<C2DM>(LinearRing.class.cast(lineStrings.toArray(new LineString[0])));
        } else if (first.getPositionClass().equals(C3D.class)) {
            return new MultiLineString<C3D>(LinearRing.class.cast(lineStrings.toArray(new LineString[0])));
        } else if (first.getPositionClass().equals(C3DM.class)) {
            return new MultiLineString<C3DM>(LinearRing.class.cast(lineStrings.toArray(new LineString[0])));
        } else {
            return new MultiLineString<C2D>(LinearRing.class.cast(lineStrings.toArray(new LineString[0])));
        }
    }

    private MultiPolygon<?> decodeMultiPolygon(int shapeIdx) {
        List<Polygon<?>> polygons = Lists.newArrayList();
        for (int i = shapeIdx; i < shapes.length; i++) {
            if (shapes[i].parentOffset == shapeIdx) {
                polygons.add(decodePolygon(i));
            }
        }

        Polygon<?> first = polygons.get(0);
        if (first.getPositionClass().equals(C2D.class)) {
            return new MultiPolygon<C2D>(Polygon.class.cast(polygons.toArray(new Polygon[0])));
        } else if (first.getPositionClass().equals(C2DM.class)) {
            return new MultiPolygon<C2DM>(Polygon.class.cast(polygons.toArray(new Polygon[0])));
        } else if (first.getPositionClass().equals(C3D.class)) {
            return new MultiPolygon<C3D>(Polygon.class.cast(polygons.toArray(new Polygon[0])));
        } else if (first.getPositionClass().equals(C3DM.class)) {
            return new MultiPolygon<C3DM>(Polygon.class.cast(polygons.toArray(new Polygon[0])));
        } else {
            return new MultiPolygon<C2D>(Polygon.class.cast(polygons.toArray(new Polygon[0])));
        }
    }

    private MultiPoint<?> decodeMultiPoint(int shapeIdx) {
        List<Point<?>> points = Lists.newArrayList();
        for (int i = shapeIdx; i < shapes.length; i++) {
            if (shapes[i].parentOffset == shapeIdx) {
                points.add(decodePoint(i));
            }
        }
        return new MultiPoint(points.toArray(new Point[0]));
    }

    private Polygon<?> decodePolygon(int shapeIdx) {
        Shape shape = shapes[shapeIdx];
        int figureOffset = shape.figureOffset;
        if (figureOffset <= -1) {
            return new Polygon<Position>();
        }
        int figureStopIdx = figures.length - 1;
        if (shapeIdx < (shapes.length - 1)) {
            figureStopIdx = shapes[shapeIdx + 1].figureOffset - 1;
        }
        List<LinearRing<?>> linearRings = Lists.newArrayList();
        for (int i = figureOffset; i <= figureStopIdx; i++) {
            PositionSequence<?> positionSequence = createPoints(i);
            if (positionSequence.getPositionClass().equals(C2D.class)) {
                linearRings.add(new LinearRing<C2D>(PositionSequence.class.cast(positionSequence), CoordinateReferenceSystems.PROJECTED_2D_METER));
            } else if (positionSequence.getPositionClass().equals(C2DM.class)) {
                linearRings.add(new LinearRing<C2DM>(PositionSequence.class.cast(positionSequence), CoordinateReferenceSystems.PROJECTED_2DM_METER));
            } else if (positionSequence.getPositionClass().equals(C3D.class)) {
                linearRings.add(new LinearRing<C3D>(PositionSequence.class.cast(positionSequence), CoordinateReferenceSystems.PROJECTED_3D_METER));
            } else if (positionSequence.getPositionClass().equals(C3DM.class)) {
                linearRings.add(new LinearRing<C3DM>(PositionSequence.class.cast(positionSequence), CoordinateReferenceSystems.PROJECTED_3DM_METER));
            } else {
                linearRings.add(new LinearRing<C2D>(CoordinateReferenceSystems.PROJECTED_2D_METER));
            }
        }
        LinearRing<?> first = linearRings.get(0);
        if (first.getPositionClass().equals(C2D.class)) {
            return new Polygon<C2D>(LinearRing.class.cast(linearRings));
        } else if (first.getPositionClass().equals(C2DM.class)) {
            return new Polygon<C2DM>(LinearRing.class.cast(linearRings));
        } else if (first.getPositionClass().equals(C3D.class)) {
            return new Polygon<C3D>(LinearRing.class.cast(linearRings));
        } else if (first.getPositionClass().equals(C3DM.class)) {
            return new Polygon<C3DM>(LinearRing.class.cast(linearRings));
        } else {
            return new Polygon<C2D>(LinearRing.class.cast(linearRings));
        }
    }

    private LineString<?> decodeLineString(int shapeIdx) {
        Shape shape = shapes[shapeIdx];
        PositionSequence<?> positionSequence = createPoints(shape.figureOffset);
        if (dimension.getClass().equals(C2D.class)) {
            C2D[] positions = new C2D[positionSequence.size()];
            for (int i = 0; i < positionSequence.size(); i++) {
                positions[i] = c(positionSequence.getPositionN(i).getCoordinate(0), positionSequence.getPositionN(i).getCoordinate(1));
            }
            return linestring(CoordinateReferenceSystems.PROJECTED_2D_METER, positions);
        } else if (dimension.getClass().equals(C2DM.class)) {
            C2DM[] positions = new C2DM[positionSequence.size()];
            for (int i = 0; i < positionSequence.size(); i++) {
                positions[i] = cM(positionSequence.getPositionN(i).getCoordinate(0),
                        positionSequence.getPositionN(i).getCoordinate(1),
                        positionSequence.getPositionN(i).getCoordinate(2));
            }
            return linestring(CoordinateReferenceSystems.PROJECTED_2DM_METER, positions);
        } else if (dimension.getClass().equals(C3D.class)) {
            C3D[] positions = new C3D[positionSequence.size()];
            for (int i = 0; i < positionSequence.size(); i++) {
                positions[i] = c(positionSequence.getPositionN(i).getCoordinate(0),
                        positionSequence.getPositionN(i).getCoordinate(1),
                        positionSequence.getPositionN(i).getCoordinate(2));
            }
            return linestring(CoordinateReferenceSystems.PROJECTED_3D_METER, positions);
        } else if (dimension.getClass().equals(C3DM.class)) {
            C3DM[] positions = new C3DM[positionSequence.size()];
            for (int i = 0; i < positionSequence.size(); i++) {
                positions[i] = c(positionSequence.getPositionN(i).getCoordinate(0),
                        positionSequence.getPositionN(i).getCoordinate(1),
                        positionSequence.getPositionN(i).getCoordinate(2),
                        positionSequence.getPositionN(i).getCoordinate(3));
            }
            return linestring(CoordinateReferenceSystems.PROJECTED_3DM_METER, positions);
        } else {
            C2D[] positions = new C2D[positionSequence.size()];
            for (int i = 0; i < positionSequence.size(); i++) {
                positions[i] = c(positionSequence.getPositionN(i).getCoordinate(0), positionSequence.getPositionN(i).getCoordinate(1));
            }
            return linestring(CoordinateReferenceSystems.PROJECTED_2D_METER, positions);
        }
    }

    private Point<?> decodePoint(int shapeIdx) {
        int pointIdx = figures[shapes[shapeIdx].figureOffset].pointOffset;
        return createPoint(pointIdx);
    }

    private Point<?> createPoint(int idx) {
        double x = points[idx][0];
        double y = points[idx][1];
        if (hasM) {
            if (hasZ) {
                return point(CoordinateReferenceSystems.PROJECTED_3DM_METER, c(x, y, zValues[idx], mValues[idx]));
            } else {
                return point(CoordinateReferenceSystems.PROJECTED_2DM_METER, cM(x, y, mValues[idx]));
            }
        } else if (hasZ) {
            return point(CoordinateReferenceSystems.PROJECTED_3D_METER, c(x, y, zValues[idx]));
        } else {
            return point(CoordinateReferenceSystems.PROJECTED_2D_METER, c(x, y));
        }
    }

    private PositionSequence<?> createPoints(int idx1, int idx2, Position dimension) {
        PositionSequenceBuilder builder;
        if (dimension.getClass().equals(C2D.class)) {
            builder = PositionSequenceBuilders.fixedSized(idx2 - idx1, C2D.class);
        } else if (dimension.getClass().equals(C2DM.class)) {
            builder = PositionSequenceBuilders.fixedSized(idx2 - idx1, C2DM.class);
        } else if (dimension.getClass().equals(C3D.class)) {
            builder = PositionSequenceBuilders.fixedSized(idx2 - idx1, C3D.class);
        } else if (dimension.getClass().equals(C3DM.class)) {
            builder = PositionSequenceBuilders.fixedSized(idx2 - idx1, C3DM.class);
        } else {
            builder = PositionSequenceBuilders.fixedSized(idx2 - idx1, C2D.class);
        }
        return builder.toPositionSequence();
    }

    private PositionSequence<?> createPoints(int figureIdx) {
        int idx1 = figures[figureIdx].pointOffset;
        int idx2 = points.length;
        if (figureIdx < (figures.length - 1)) {
            idx2 = figures[figureIdx + 1].pointOffset;
        }
        return createPoints(idx1, idx2, dimension);
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
