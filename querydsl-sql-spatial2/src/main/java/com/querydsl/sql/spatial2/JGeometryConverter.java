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

import oracle.spatial.geometry.JGeometry;
import org.geolatte.geom.*;
import org.geolatte.geom.Point;
import org.geolatte.geom.crs.CoordinateReferenceSystems;

import static oracle.spatial.geometry.JGeometry.*;
import static org.geolatte.geom.builder.DSL.*;

/**
 * A converter for oracle spatial.
 *
 * @author Timo Westkämper
 * @author Nikita Kochkurov (kochkurovn@gmail.com)
 */
final class JGeometryConverter {

    // to jgeometry

    public static JGeometry convert(Geometry<?> geometry) {
        switch (geometry.getGeometryType()) {
        case POINT: return convert((Point<?>) geometry);
        case GEOMETRYCOLLECTION: return convert((GeometryCollection<?>) geometry);
        case LINESTRING: return convert((LineString<?>) geometry);
        case LINEARRING: return convert((LinearRing<?>) geometry);
        case POLYGON: return convert((Polygon<?>) geometry);
        case MULTIPOINT: return convert((MultiPoint<?>) geometry);
        case MULTIPOLYGON: return convert((MultiPolygon<?>) geometry);
        case MULTILINESTRING: return convert((MultiLineString<?>) geometry);
        default: throw new IllegalArgumentException(geometry.toString());
        }
    }

    private static double[] getPoints(PositionSequence<?> positionSequence) {
        int dim = positionSequence.getCoordinateDimension();
        double[] values = new double[positionSequence.size() * dim];
        int offset = 0;
        for (int i = 0; i < positionSequence.size(); i++) {
            values[offset++] = positionSequence.getPositionN(i).getCoordinate(0);
            values[offset++] = positionSequence.getPositionN(i).getCoordinate(1);
            if (dim >= 3) {
                values[offset++] = positionSequence.getPositionN(i).getCoordinate(2);
            }
            if (dim == 4) {
                values[offset++] = positionSequence.getPositionN(i).getCoordinate(3);
            }
        }
        return values;
    }

    private static double[] getCoordinates(Point<?> geometry) {
        double[] value = new double[geometry.getCoordinateDimension()];
        int offset = 0;
        int dimension = geometry.getDimension();
        value[offset++] = geometry.getPositionN(0).getCoordinate(0);
        value[offset++] = geometry.getPositionN(0).getCoordinate(1);
        if (dimension == 3) {
            value[offset++] = geometry.getPositionN(0).getCoordinate(2);
        }
        if (dimension == 4) {
            value[offset++] = geometry.getPositionN(0).getCoordinate(dimension - 1);
        }
        return value;
    }

    private static JGeometry convert(Polygon<?> geometry) {
        int srid = geometry.getSRID();
        int dim = geometry.getCoordinateDimension();
        double[] points = getPoints(geometry.getPositions());
        int[] elemInfo = new int[3 + geometry.getNumInteriorRing() * 3];
        int offset = 0;
        int pointOffset = 1;
        elemInfo[offset++] = pointOffset;
        elemInfo[offset++] = 1003; // exterior
        elemInfo[offset++] = 1;
        pointOffset += geometry.getExteriorRing().getNumPositions() * dim;
        for (int i = 0; i < geometry.getNumInteriorRing(); i++) {
            elemInfo[offset++] = pointOffset;
            elemInfo[offset++] = 2003;
            elemInfo[offset++] = 1;
            pointOffset += geometry.getInteriorRingN(i).getNumPositions() * dim;
        }
        int gtype = dim * 1000 + (geometry instanceof Measured ? dim : 0) * 100 + GTYPE_POLYGON;
        return new JGeometry(gtype, srid, elemInfo, points);
    }


    private static JGeometry convert(LineString<?> geometry) {
        int srid = geometry.getSRID();
        int dim = geometry.getCoordinateDimension();
        double[] points = getPoints(geometry.getPositions());
        int[] elemInfo = new int[]{1, 2, 1};
        int gtype = dim * 1000 + (geometry instanceof Measured ? dim : 0) * 100 + GTYPE_CURVE;
        return new JGeometry(gtype, srid, elemInfo, points);
    }

    private static JGeometry convert(GeometryCollection<?> geometry) {
        // TODO
        throw new UnsupportedOperationException();
    }

    private static JGeometry convert(MultiPoint<?> geometry) {
        int srid = geometry.getSRID();
        int dim = geometry.getCoordinateDimension();
        double[] points = getPoints(geometry.getPositions());
        int[] elemInfo = new int[]{1, 1, geometry.getNumPositions()};
        int gtype = dim * 1000 + (geometry instanceof Measured ? dim : 0) * 100 + GTYPE_MULTIPOINT;
        return new JGeometry(gtype, srid, elemInfo, points);
    }

    private static JGeometry convert(MultiPolygon<?> geometry) {
        // TODO
        throw new UnsupportedOperationException();
    }

    private static JGeometry convert(MultiLineString<?> geometry) {
        int srid = geometry.getSRID();
        int dim = geometry.getCoordinateDimension();
        double[] points = getPoints(geometry.getPositions());
        int[] elemInfo = new int[geometry.getNumGeometries() * 3];
        int offset = 0;
        int pointOffset = 1;
        for (int i = 0; i < geometry.getNumGeometries(); i++) {
            elemInfo[offset++] = pointOffset;
            elemInfo[offset++] = 2;
            elemInfo[offset++] = 1;
            pointOffset += geometry.getGeometryN(i).getNumPositions() * dim;
        }
        int gtype = dim * 1000 + (geometry instanceof Measured ? dim : 0) * 100 + GTYPE_MULTICURVE;
        return new JGeometry(gtype, srid, elemInfo, points);
    }

    private static JGeometry convert(Point<?> geometry) {
        double[] value = getCoordinates(geometry);
        int srid = geometry.getSRID();
        if (geometry instanceof Measured) {
            return JGeometry.createLRSPoint(value, value.length - 1, srid);
        } else {
            return JGeometry.createPoint(value, value.length, srid);
        }
    }

    // to geolatte

    public static Geometry<?> convert(JGeometry geometry) {
        switch (geometry.getType()) {
        case GTYPE_COLLECTION: return convertCollection(geometry);
        case GTYPE_CURVE: return convertCurve(geometry);
        case GTYPE_MULTICURVE: return convertMultiCurve(geometry);
        case GTYPE_MULTIPOINT: return convertMultiPoint(geometry);
        case GTYPE_MULTIPOLYGON: return convertMultiPolygon(geometry);
        case GTYPE_POINT: return convertPoint(geometry);
        case GTYPE_POLYGON: return convertPolygon(geometry);
        default: throw new IllegalArgumentException(geometry.toString());
        }
    }

    private static PositionSequence<?> getPoints(JGeometry geometry) {
        int dimensions = geometry.getDimensions();
        boolean measured = geometry.isLRSGeometry();
        double[] ordinates = geometry.getOrdinatesArray();
        PositionSequenceBuilder builder;
        if (dimensions == 2) {
            builder = PositionSequenceBuilders.fixedSized(ordinates.length, C2D.class);
        } else if (dimensions == 3) {
             if (measured) {
                 builder = PositionSequenceBuilders.fixedSized(ordinates.length, C2DM.class);
             } else {
                 builder = PositionSequenceBuilders.fixedSized(ordinates.length, C3D.class);
             }
        } else if (dimensions == 4) {
            builder = PositionSequenceBuilders.fixedSized(ordinates.length, C3DM.class);
        } else {
            builder = PositionSequenceBuilders.fixedSized(ordinates.length, C2D.class);
        }
        for (int i = 0; i < ordinates.length; i++) {
            builder.add(ordinates[i]);
        }
        return builder.toPositionSequence();
    }

    private static Polygon<?> convertPolygon(JGeometry geometry) {
        int dimensions = geometry.getDimensions();
        boolean measured = geometry.isLRSGeometry();
        Object[] elements = geometry.getOrdinatesOfElements();
        PositionSequenceBuilder builder;
        LinearRing<?>[] rings = new LinearRing[elements.length];
        for (int i = 0; i < elements.length; i++) {
            builder = getPositionSequenceBuilder(dimensions, measured, elements.length);
            if (dimensions == 2) {
                rings[i] = new LinearRing<C2D>(builder.toPositionSequence(), CoordinateReferenceSystems.PROJECTED_2D_METER);
            } else if (dimensions == 3) {
                if (measured) {
                    rings[i] = new LinearRing<C2DM>(builder.toPositionSequence(), CoordinateReferenceSystems.PROJECTED_2DM_METER);
                } else {
                    rings[i] = new LinearRing<C3D>(builder.toPositionSequence(), CoordinateReferenceSystems.PROJECTED_3D_METER);
                }
            } else if (dimensions == 4) {
                rings[i] = new LinearRing<C3DM>(builder.toPositionSequence(), CoordinateReferenceSystems.PROJECTED_3DM_METER);
            }
        }
        LinearRing<?> first = rings[0];
        if (first.getPositionClass().equals(C2D.class)) {
            return new Polygon<C2D>(LinearRing.class.cast(rings));
        } else if (first.getPositionClass().equals(C2DM.class)) {
            return new Polygon<C2DM>(LinearRing.class.cast(rings));
        } else if (first.getPositionClass().equals(C3D.class)) {
            return new Polygon<C3D>(LinearRing.class.cast(rings));
        } else if (first.getPositionClass().equals(C3DM.class)) {
            return new Polygon<C3DM>(LinearRing.class.cast(rings));
        } else {
            return new Polygon<C2D>(LinearRing.class.cast(rings));
        }
    }

    private static PositionSequenceBuilder<?> getPositionSequenceBuilder(int dimensions, boolean measured, int size) {
        PositionSequenceBuilder builder;
        if (dimensions == 2) {
            builder = PositionSequenceBuilders.fixedSized(size, C2D.class);
        } else if (dimensions == 3) {
            if (measured) {
                builder = PositionSequenceBuilders.fixedSized(size, C2DM.class);
            } else {
                builder = PositionSequenceBuilders.fixedSized(size, C3D.class);
            }
        } else if (dimensions == 4) {
            builder = PositionSequenceBuilders.fixedSized(size, C3DM.class);
        } else {
            builder = PositionSequenceBuilders.fixedSized(size, C2D.class);
        }
        return builder;
    }

    private static Point<?> convertPoint(JGeometry geometry) {
        double[] points = geometry.getPoint();
        int dimensions = geometry.getDimensions();
        boolean measured = geometry.isLRSGeometry();
        if (dimensions == 2) {
            return point(CoordinateReferenceSystems.PROJECTED_2D_METER, c(points[0], points[1]));
        } else if (dimensions == 3) {
            return measured ? point(CoordinateReferenceSystems.PROJECTED_2DM_METER, cM(points[0], points[1], points[2])) :
                    point(CoordinateReferenceSystems.PROJECTED_3D_METER, c(points[0], points[1], points[2]));
        } else if (dimensions == 4) {
            return point(CoordinateReferenceSystems.PROJECTED_3DM_METER, c(points[0], points[1], points[2], points[3]));
        }
        return new Point<C2D>(CoordinateReferenceSystems.PROJECTED_2D_METER);
    }

    private static LineString<?> convertCurve(JGeometry geometry) {
        PositionSequence<?> positionSequence = getPoints(geometry);
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
    }

    private static MultiPoint<?> convertMultiPoint(JGeometry geometry) {
        JGeometry[] elements = geometry.getElements();
        if (elements == null || elements.length == 0) {
            return new MultiPoint<C2D>();
        }
        Point<?>[] points = new Point[elements.length];
        int dimensions = geometry.getDimensions();
        double[] ordinates = geometry.getOrdinatesArray();
        boolean measured = geometry.isLRSGeometry();
        int offset = 0;
        for (int i = 0; i < points.length; i++) {
            double[] coords = new double[dimensions];
            System.arraycopy(ordinates, offset, coords, 0, coords.length);
            points[i] = getPoint(dimensions, measured, coords);
            offset += dimensions;
        }
        if (dimensions == 2) {
            return new MultiPoint<C2D>(Point.class.cast(points));
        } else if (dimensions == 3) {
            if (measured) {
                return new MultiPoint<C2DM>(Point.class.cast(points));
            } else {
                return new MultiPoint<C3D>(Point.class.cast(points));
            }
        } else if (dimensions == 4) {
            return new MultiPoint<C3DM>(Point.class.cast(points));
        } else {
            return new MultiPoint<C2D>(Point.class.cast(points));
        }
    }


    private static Point<?> getPoint(int dimensions, boolean measured, double[] coords) {
        if (dimensions == 2) {
            return point(CoordinateReferenceSystems.PROJECTED_2D_METER, c(coords[0], coords[1]));
        } else if (dimensions == 3) {
            return measured ? point(CoordinateReferenceSystems.PROJECTED_2DM_METER, cM(coords[0], coords[1], coords[2])) :
                    point(CoordinateReferenceSystems.PROJECTED_3D_METER, c(coords[0], coords[1], coords[2]));
        } else if (dimensions == 4) {
            return point(CoordinateReferenceSystems.PROJECTED_3DM_METER, c(coords[0], coords[1], coords[2], coords[3]));
        }
        return new Point<C2D>(CoordinateReferenceSystems.PROJECTED_2D_METER);
    }

    // FIXME
    private static MultiPolygon<?> convertMultiPolygon(JGeometry geometry) {
        JGeometry[] elements = geometry.getElements();
        if (elements == null || elements.length == 0) {
            return new MultiPolygon<C2D>();
        }
        Polygon<?>[] polygons = new Polygon[elements.length];
        for (int i = 0; i < elements.length; i++) {
            polygons[i] = convertPolygon(elements[i]);
        }
        int dimensions = geometry.getDimensions();
        boolean measured = geometry.isLRSGeometry();
        if (dimensions == 2) {
            return new MultiPolygon<C2D>(Polygon.class.cast(polygons));
        } else if (dimensions == 3) {
            if (measured) {
                return new MultiPolygon<C2DM>(Polygon.class.cast(polygons));
            } else {
                return new MultiPolygon<C3D>(Polygon.class.cast(polygons));
            }
        } else if (dimensions == 4) {
            return new MultiPolygon<C3DM>(Polygon.class.cast(polygons));
        } else {
            return new MultiPolygon<C2D>(Polygon.class.cast(polygons));
        }
    }

    // FIXME
    private static MultiLineString<?> convertMultiCurve(JGeometry geometry) {
        JGeometry[] elements = geometry.getElements();
        int dimensions = geometry.getDimensions();
        boolean measured = geometry.isLRSGeometry();
        PositionSequenceBuilder builder;
        if (elements == null || elements.length == 0) {
            return new MultiLineString<C2D>();
        }
        LineString<?>[] lineStrings = new LineString[elements.length];
        for (int i = 0; i < elements.length; i++) {
            builder = getPositionSequenceBuilder(dimensions, measured, elements.length);
            if (dimensions == 2) {
                lineStrings[i] = new LinearRing<C2D>(builder.toPositionSequence(), CoordinateReferenceSystems.PROJECTED_2D_METER);
            } else if (dimensions == 3) {
                if (measured) {
                    lineStrings[i] = new LinearRing<C2DM>(builder.toPositionSequence(), CoordinateReferenceSystems.PROJECTED_2DM_METER);
                } else {
                    lineStrings[i] = new LinearRing<C3D>(builder.toPositionSequence(), CoordinateReferenceSystems.PROJECTED_3D_METER);
                }
            } else if (dimensions == 4) {
                lineStrings[i] = new LinearRing<C3DM>(builder.toPositionSequence(), CoordinateReferenceSystems.PROJECTED_3DM_METER);
            }
        }
        LineString<?> first = lineStrings[0];
        if (first.getPositionClass().equals(C2D.class)) {
            return new MultiLineString<C2D>(LinearRing.class.cast(lineStrings));
        } else if (first.getPositionClass().equals(C2DM.class)) {
            return new MultiLineString<C2DM>(LinearRing.class.cast(lineStrings));
        } else if (first.getPositionClass().equals(C3D.class)) {
            return new MultiLineString<C3D>(LinearRing.class.cast(lineStrings));
        } else if (first.getPositionClass().equals(C3DM.class)) {
            return new MultiLineString<C3DM>(LinearRing.class.cast(lineStrings));
        } else {
            return new MultiLineString<C2D>(LinearRing.class.cast(lineStrings));
        }
    }

    // FIXME
    private static GeometryCollection<?> convertCollection(JGeometry geometry) {
        JGeometry[] elements = geometry.getElements();
        if (elements == null || elements.length == 0) {
            return new GeometryCollection<Position>();
        }
        Geometry<?>[] geometries = new Geometry[elements.length];
        for (int i = 0; i < elements.length; i++) {
            geometries[i] = convert(elements[i]);
        }
        Geometry<?> first = geometries[0];
        if (first instanceof Point) {
            return new GeometryCollection<Position>((Point.class.cast(geometries)));
        } else if (first instanceof LinearRing) {
            return new GeometryCollection<Position>((LinearRing.class.cast(geometries)));
        } else if (first instanceof LineString) {
            return new GeometryCollection<Position>((LineString.class.cast(geometries)));
        } else if (first instanceof MultiLineString) {
            return new GeometryCollection<Position>((MultiLineString.class.cast(geometries)));
        } else if (first instanceof Polygon) {
            return new GeometryCollection<Position>((Polygon.class.cast(geometries)));
        } else if (first instanceof MultiPoint) {
            return new GeometryCollection<Position>((MultiPoint.class.cast(geometries)));
        } else if (first instanceof MultiPolygon) {
            return new GeometryCollection<Position>((MultiPolygon.class.cast(geometries)));
        } else if (first instanceof GeometryCollection) {
            return new GeometryCollection<Position>((GeometryCollection.class.cast(geometries)));
        } else {
            throw new IllegalArgumentException(geometry.getClass().getName());
        }
    }

    private JGeometryConverter() { }

}
