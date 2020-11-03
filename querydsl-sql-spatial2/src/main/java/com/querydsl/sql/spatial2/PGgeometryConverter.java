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

import org.geolatte.geom.*;
import org.geolatte.geom.crs.*;

import static org.geolatte.geom.builder.DSL.*;

/**
 * A converter for postgis.
 *
 * @author Timo Westkämper
 * @author Nikita Kochkurov (kochkurovn@gmail.com)
 */
final class PGgeometryConverter {

    // to postgis

    public static org.postgis.Geometry convert(Geometry<?> geometry) {
        if (geometry instanceof Point) {
            return convertPoint((Point<?>) geometry);
        } else if (geometry instanceof LinearRing) {
            return convert((LinearRing<?>) geometry);
        } else if (geometry instanceof LineString) {
            return convert((LineString<?>) geometry);
        } else if (geometry instanceof MultiLineString) {
            return convert((MultiLineString<?>) geometry);
        } else if (geometry instanceof Polygon) {
            return convert((Polygon<?>) geometry);
        } else if (geometry instanceof MultiPoint) {
            return convert((MultiPoint<?>) geometry);
        } else if (geometry instanceof MultiPolygon) {
            return convert((MultiPolygon<?>) geometry);
        } else if (geometry instanceof GeometryCollection) {
            return convert((GeometryCollection<?>) geometry);
        } else {
            throw new IllegalArgumentException(geometry.getClass().getName());
        }
    }

    private static org.postgis.Point convertPoint(Point<?> point) {
        org.postgis.Point pgPoint = new org.postgis.Point();
        int dimension = point.getDimension();
        pgPoint.srid = point.getSRID();
        pgPoint.dimension = dimension;
        pgPoint.haveMeasure = point instanceof Measured;
        pgPoint.x = point.getPositionN(0).getCoordinate(0);
        pgPoint.y = point.getPositionN(0).getCoordinate(1);
        if (dimension >= 3) {
            pgPoint.z = point.getPositionN(0).getCoordinate(2);
        }
        if (dimension == 4) {
            pgPoint.m = point.getPositionN(0).getCoordinate(dimension - 1);
        }
        return pgPoint;
    }

    private static org.postgis.Point convert(Position point, int srid) {
        org.postgis.Point pgPoint = new org.postgis.Point();
        int dimension = point.getCoordinateDimension();
        pgPoint.srid = srid;
        pgPoint.dimension = dimension;
        pgPoint.haveMeasure = point instanceof Measured;
        pgPoint.x = point.getCoordinate(0);
        pgPoint.y = point.getCoordinate(1);
        if (dimension >= 3) {
            pgPoint.z = point.getCoordinate(2);
        }
        if (dimension == 4) {
            pgPoint.m = point.getCoordinate(dimension - 1);
        }
        return pgPoint;
    }

    private static <T> org.postgis.Point convertCoordinate(T t, int srid) {
        if (t instanceof Position) {
            return convert((Position) t, srid);
        }
        return new org.postgis.Point();
    }

    private static org.postgis.Point[] convertPoints(LineString<?> lineString, int srid) {
        org.postgis.Point[] pgPoints = new org.postgis.Point[lineString.getNumPositions()];
        for (int i = 0; i < pgPoints.length; i++) {
            pgPoints[i] = convertCoordinate(lineString.getPositionN(i), srid);
        }
        return pgPoints;
    }

    private static org.postgis.Point[] convertPoints(PositionSequence<?> positionSequence, int srid) {
        org.postgis.Point[] pgPoints = new org.postgis.Point[positionSequence.size()];
        for (int i = 0; i < positionSequence.size(); i++) {
            pgPoints[i] = convertCoordinate(positionSequence.getPositionN(i), srid);
        }
        return pgPoints;
    }


    private static org.postgis.LineString convert(LineString<?> lineString) {
        org.postgis.Point[] pgPoints = convertPoints(lineString, lineString.getSRID());
        org.postgis.LineString pgLineString = new org.postgis.LineString(pgPoints);
        pgLineString.haveMeasure = lineString instanceof Measured;
        pgLineString.setSrid(lineString.getSRID());
        return pgLineString;
    }

    private static org.postgis.LinearRing convert(LinearRing<?> linearRing) {
        org.postgis.Point[] pgPoints = convertPoints(linearRing, linearRing.getSRID());
        org.postgis.LinearRing pgLinearRing = new org.postgis.LinearRing(pgPoints);
        pgLinearRing.haveMeasure = linearRing instanceof Measured;
        pgLinearRing.setSrid(linearRing.getSRID());
        return pgLinearRing;
    }

    private static org.postgis.MultiLineString convert(MultiLineString<?> multiLineString) {
        org.postgis.LineString[] pgLineStrings = new org.postgis.LineString[multiLineString.getNumGeometries()];
        for (int i = 0; i < pgLineStrings.length; i++) {
            pgLineStrings[i] = convert(multiLineString.getGeometryN(i));
        }
        org.postgis.MultiLineString pgMultiLineString = new org.postgis.MultiLineString(pgLineStrings);
        pgMultiLineString.haveMeasure = multiLineString instanceof Measured;
        pgMultiLineString.setSrid(multiLineString.getSRID());
        return pgMultiLineString;
    }

    private static org.postgis.Polygon convert(Polygon<?> polygon) {
        int numRings = polygon.getNumInteriorRing();
        org.postgis.LinearRing[] rings = new org.postgis.LinearRing[numRings + 1];
        rings[0] = convert(polygon.getExteriorRing());
        for (int i = 0; i < numRings; i++) {
            rings[i + 1] = convert(polygon.getInteriorRingN(i));
        }
        org.postgis.Polygon pgPolygon = new org.postgis.Polygon(rings);
        pgPolygon.setSrid(polygon.getSRID());
        return pgPolygon;
    }

    private static org.postgis.MultiPoint convert(MultiPoint<?> multiPoint) {
        org.postgis.Point[] pgPoints = convertPoints(multiPoint.getPositions(), multiPoint.getSRID());
        org.postgis.MultiPoint pgMultiPoint = new org.postgis.MultiPoint(pgPoints);
        pgMultiPoint.setSrid(multiPoint.getSRID());
        return pgMultiPoint;
    }

    private static org.postgis.MultiPolygon convert(MultiPolygon<?> multiPolygon) {
        org.postgis.Polygon[] pgPolygons = new org.postgis.Polygon[multiPolygon.getNumGeometries()];
        for (int i = 0; i < pgPolygons.length; i++) {
            pgPolygons[i] = convert(multiPolygon.getGeometryN(i));
        }
        org.postgis.MultiPolygon pgMultiPolygon = new org.postgis.MultiPolygon(pgPolygons);
        pgMultiPolygon.setSrid(multiPolygon.getSRID());
        return pgMultiPolygon;
    }

    private static org.postgis.GeometryCollection convert(GeometryCollection<?> geometryCollection) {
        org.postgis.Geometry[] pgGeometries = new org.postgis.Geometry[geometryCollection.getNumGeometries()];
        for (int i = 0; i < pgGeometries.length; i++) {
            pgGeometries[i] = convert(geometryCollection.getGeometryN(i));
        }
        org.postgis.GeometryCollection pgGeometryCollection = new org.postgis.GeometryCollection(pgGeometries);
        pgGeometryCollection.setSrid(geometryCollection.getSRID());
        return pgGeometryCollection;
    }

    // to geolatte

    public static Geometry<?> convert(org.postgis.Geometry geometry) {
        switch (geometry.getType()) {
        case org.postgis.Geometry.POINT:
            return convert((org.postgis.Point) geometry);
        case org.postgis.Geometry.LINESTRING:
            return convert((org.postgis.LineString) geometry);
        case org.postgis.Geometry.LINEARRING:
            return convert((org.postgis.LinearRing) geometry);
        case org.postgis.Geometry.POLYGON:
            return convert((org.postgis.Polygon) geometry);
        case org.postgis.Geometry.MULTILINESTRING:
            return convert((org.postgis.MultiLineString) geometry);
        case org.postgis.Geometry.MULTIPOINT:
            return convert((org.postgis.MultiPoint) geometry);
        case org.postgis.Geometry.MULTIPOLYGON:
            return convert((org.postgis.MultiPolygon) geometry);
        case org.postgis.Geometry.GEOMETRYCOLLECTION:
            return convert((org.postgis.GeometryCollection) geometry);
        }
        throw new IllegalArgumentException(geometry.toString());
    }

    private static Point<?> convert(org.postgis.Point geometry) {
        double[] points = convertPoints(geometry);
        if (points.length == 2) {
            return point(CoordinateReferenceSystems.PROJECTED_2D_METER, c(points[0], points[1]));
        } else if (points.length == 3) {
            return geometry.haveMeasure ? point(CoordinateReferenceSystems.PROJECTED_2DM_METER, cM(points[0], points[1], points[2])) :
                    point(CoordinateReferenceSystems.PROJECTED_3D_METER, c(points[0], points[1], points[2]));
        } else if (points.length == 4) {
            return point(CoordinateReferenceSystems.PROJECTED_3DM_METER, c(points[0], points[1], points[2], points[3]));
        }
        return new Point<C2D>(CoordinateReferenceSystems.PROJECTED_2D_METER);
    }

    private static double[] convertPoints(org.postgis.Point geometry) {
        int d = geometry.dimension;
        double[] points = new double[d + (geometry.haveMeasure ? 1 : 0)];
        int offset = 0;
        points[offset++] = geometry.x;
        points[offset++] = geometry.y;
        if (d == 3) {
            points[offset++] = geometry.z;
        }
        if (geometry.haveMeasure) {
            points[offset++] = geometry.m;
        }
        return points;
    }

    private static PositionSequence<?> convertPoints(org.postgis.Point[] points) {
        PositionSequenceBuilder builder;
        if (points.length == 0) {
            builder = PositionSequenceBuilders.fixedSized(0,  C2D.class);
            return builder.toPositionSequence();
        }
        org.postgis.Point first = points[0];
        Position position = getPosition(first, first.haveMeasure);
        builder = PositionSequenceBuilders.fixedSized(points.length, position.getClass());
        for (int i = 0; i < points.length; i++) {
            builder.add(convertPoints(points[i]));
        }
        return builder.toPositionSequence();
    }

    public static Position getPosition(org.postgis.Point point, boolean haveMeasure) {
        double[] points = convertPoints(point);
        if (points.length == 2) {
            return c(points[0], points[1]);
        } else if (points.length == 3) {
            return haveMeasure ? cM(points[0], points[1], points[2]) : c(points[0], points[1], points[2]);
        } else if (points.length == 4) {
            return c(points[0], points[1], points[2], points[3]);
        }
        return new C2D();
    }

    private static GeometryCollection<?> convert(org.postgis.GeometryCollection geometry) {
        Geometry<?>[] geometries = new Geometry[geometry.numGeoms()];
        for (int i = 0; i < geometries.length; i++) {
            geometries[i] = convert(geometry.getSubGeometry(i));
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

    private static MultiPolygon<?> convert(org.postgis.MultiPolygon geometry) {
        Polygon<?>[] polygons = new Polygon[geometry.numPolygons()];
        for (int i = 0; i < polygons.length; i++) {
            polygons[i] = convert(geometry.getPolygon(i));
        }
        Polygon<?> first = polygons[0];
        if (first.getPositionClass().equals(C2D.class)) {
            return new MultiPolygon<C2D>(Polygon.class.cast(polygons));
        } else if (first.getPositionClass().equals(C2DM.class)) {
            return new MultiPolygon<C2DM>(Polygon.class.cast(polygons));
        } else if (first.getPositionClass().equals(C3D.class)) {
            return new MultiPolygon<C3D>(Polygon.class.cast(polygons));
        } else if (first.getPositionClass().equals(C3DM.class)) {
            return new MultiPolygon<C3DM>(Polygon.class.cast(polygons));
        } else {
            return new MultiPolygon<C2D>(Polygon.class.cast(polygons));
        }
    }

    private static MultiPoint<?> convert(org.postgis.MultiPoint geometry) {
        Point<?>[] points = new Point[geometry.numPoints()];
        for (int i = 0; i < points.length; i++) {
            points[i] = convert(geometry.getPoint(i));
        }
        Point<?> first = points[0];
        if (first.getPositionClass().equals(C2D.class)) {
            return new MultiPoint<C2D>(Point.class.cast(points));
        } else if (first.getPositionClass().equals(C2DM.class)) {
            return new MultiPoint<C2DM>(Point.class.cast(points));
        } else if (first.getPositionClass().equals(C3D.class)) {
            return new MultiPoint<C3D>(Point.class.cast(points));
        } else if (first.getPositionClass().equals(C3DM.class)) {
            return new MultiPoint<C3DM>(Point.class.cast(points));
        } else {
            return new MultiPoint<C2D>(Point.class.cast(points));
        }
    }

    private static MultiLineString<?> convert(org.postgis.MultiLineString geometry) {
        LineString<?>[] lineStrings = new LineString[geometry.numLines()];
        for (int i = 0; i < lineStrings.length; i++) {
            lineStrings[i] = convert(geometry.getLine(i));
        }
        LineString<?> first = lineStrings[0];
        if (first.getPositionClass().equals(C2D.class)) {
            return new MultiLineString<C2D>(LineString.class.cast(lineStrings));
        } else if (first.getPositionClass().equals(C2DM.class)) {
            return new MultiLineString<C2DM>(LineString.class.cast(lineStrings));
        } else if (first.getPositionClass().equals(C3D.class)) {
            return new MultiLineString<C3D>(LineString.class.cast(lineStrings));
        } else if (first.getPositionClass().equals(C3DM.class)) {
            return new MultiLineString<C3DM>(LineString.class.cast(lineStrings));
        } else {
            return new MultiLineString<C2D>(LineString.class.cast(lineStrings));
        }
    }

    private static Polygon<?> convert(org.postgis.Polygon geometry) {
        LinearRing<?>[] rings = new LinearRing[geometry.numRings()];
        for (int i = 0; i < rings.length; i++) {
            rings[i] = convert(geometry.getRing(i));
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

    private static LinearRing<?> convert(org.postgis.LinearRing geometry) {
        PositionSequence<?> positionSequence = convertPoints(geometry.getPoints());
        if (positionSequence.getPositionClass().equals(C2D.class)) {
            return new LinearRing<C2D>(PositionSequence.class.cast(positionSequence), CoordinateReferenceSystems.PROJECTED_2D_METER);
        } else if (positionSequence.getPositionClass().equals(C2DM.class)) {
            return new LinearRing<C2DM>(PositionSequence.class.cast(positionSequence), CoordinateReferenceSystems.PROJECTED_2DM_METER);
        } else if (positionSequence.getPositionClass().equals(C3D.class)) {
            return new LinearRing<C3D>(PositionSequence.class.cast(positionSequence), CoordinateReferenceSystems.PROJECTED_3D_METER);
        } else if (positionSequence.getPositionClass().equals(C3DM.class)) {
            return new LinearRing<C3DM>(PositionSequence.class.cast(positionSequence), CoordinateReferenceSystems.PROJECTED_3DM_METER);
        } else {
            return new LinearRing<C2D>(CoordinateReferenceSystems.PROJECTED_2D_METER);
        }
    }


    private static LineString<?> convert(org.postgis.LineString geometry) {
        PositionSequence<?> positionSequence = convertPoints(geometry.getPoints());
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

    private PGgeometryConverter() { }

}
