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

import org.geolatte.geom.*;
import org.geolatte.geom.crs.CoordinateReferenceSystem;
import org.geolatte.geom.crs.CoordinateReferenceSystems;
import org.geolatte.geom.crs.CrsRegistry;

final class PGgeometryConverter {

    // to postgis

    public static org.postgis.Geometry convert(Geometry geometry) {
        if (geometry instanceof Point) {
            return convert((Point) geometry);
        } else if (geometry instanceof LinearRing) {
            return convert((LinearRing) geometry);
        } else if (geometry instanceof LineString) {
            return convert((LineString) geometry);
        } else if (geometry instanceof MultiLineString) {
            return convert((MultiLineString) geometry);
        } else if (geometry instanceof Polygon) {
            return convert((Polygon) geometry);
        } else if (geometry instanceof MultiPoint) {
            return convert((MultiPoint) geometry);
        } else if (geometry instanceof MultiPolygon) {
            return convert((MultiPolygon) geometry);
        } else if (geometry instanceof GeometryCollection) {
            return convert((GeometryCollection) geometry);
        } else {
            throw new IllegalArgumentException(geometry.getClass().getName());
        }
    }

    private static org.postgis.Point convert(Point point) {
        return convert(point.getPosition(), point.getSRID());
    }

    private static org.postgis.Point convert(Position position, int srid) {
        org.postgis.Point pgPoint = new org.postgis.Point();
        pgPoint.srid = srid;

        if (position instanceof C2D) {
            pgPoint.x = ((C2D) position).getX();
            pgPoint.y = ((C2D) position).getY();

            if (position instanceof C3D) {
                pgPoint.z = ((C3D) position).getZ();
                pgPoint.dimension = 3;
            } else {
                pgPoint.dimension = 2;
            }

        } else if (position instanceof G2D) {
            pgPoint.x = ((G2D) position).getLon();
            pgPoint.y = ((G2D) position).getLat();

            if (position instanceof G3D) {
                pgPoint.z = ((G3D) position).getHeight();
                pgPoint.dimension = 3;
            } else {
                pgPoint.dimension = 2;
            }
        }

        if (position instanceof Measured) {
            pgPoint.m = ((Measured) position).getM();
            pgPoint.haveMeasure = true;
        }

        return pgPoint;
    }

    private static org.postgis.Point[] convertPoints(Geometry geometry) {
        org.postgis.Point[] pgPoints = new org.postgis.Point[geometry.getNumPositions()];
        for (int i = 0; i < pgPoints.length; i++) {
            pgPoints[i] = convert(geometry.getPositionN(i), geometry.getSRID());
        }
        return pgPoints;
    }

    private static org.postgis.LineString convert(LineString lineString) {
        org.postgis.Point[] pgPoints = convertPoints(lineString);
        org.postgis.LineString pgLineString = new org.postgis.LineString(pgPoints);
        pgLineString.haveMeasure = lineString.getPositions().getPositionFactory().hasMComponent();
        pgLineString.setSrid(lineString.getSRID());
        return pgLineString;
    }

    private static org.postgis.LinearRing convert(LinearRing linearRing) {
        org.postgis.Point[] pgPoints = convertPoints(linearRing);
        org.postgis.LinearRing pgLinearRing = new org.postgis.LinearRing(pgPoints);
        pgLinearRing.haveMeasure = linearRing.getPositions().getPositionFactory().hasMComponent();
        pgLinearRing.setSrid(linearRing.getSRID());
        return pgLinearRing;
    }

    private static org.postgis.MultiLineString convert(MultiLineString multiLineString) {
        org.postgis.LineString[] pgLineStrings = new org.postgis.LineString[multiLineString.getNumGeometries()];
        for (int i = 0; i < pgLineStrings.length; i++) {
            pgLineStrings[i] = convert((LineString) multiLineString.getGeometryN(i));
        }
        org.postgis.MultiLineString pgMultiLineString = new org.postgis.MultiLineString(pgLineStrings);
        pgMultiLineString.haveMeasure = multiLineString.getPositions().getPositionFactory().hasMComponent();
        pgMultiLineString.setSrid(multiLineString.getSRID());
        return pgMultiLineString;
    }

    private static org.postgis.Polygon convert(Polygon polygon) {
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

    private static org.postgis.MultiPoint convert(MultiPoint multiPoint) {
        org.postgis.Point[] pgPoints = convertPoints(multiPoint);
        org.postgis.MultiPoint pgMultiPoint = new org.postgis.MultiPoint(pgPoints);
        pgMultiPoint.setSrid(multiPoint.getSRID());
        return pgMultiPoint;
    }

    private static org.postgis.MultiPolygon convert(MultiPolygon multiPolygon) {
        org.postgis.Polygon[] pgPolygons = new org.postgis.Polygon[multiPolygon.getNumGeometries()];
        for (int i = 0; i < pgPolygons.length; i++) {
            pgPolygons[i] = convert((Polygon) multiPolygon.getGeometryN(i));
        }
        org.postgis.MultiPolygon pgMultiPolygon = new org.postgis.MultiPolygon(pgPolygons);
        pgMultiPolygon.setSrid(multiPolygon.getSRID());
        return pgMultiPolygon;
    }

    private static org.postgis.GeometryCollection convert(GeometryCollection geometryCollection) {
        org.postgis.Geometry[] pgGeometries = new org.postgis.Geometry[geometryCollection.getNumGeometries()];
        for (int i = 0; i < pgGeometries.length; i++) {
            pgGeometries[i] = convert(geometryCollection.getGeometryN(i));
        }
        org.postgis.GeometryCollection pgGeometryCollection = new org.postgis.GeometryCollection(pgGeometries);
        pgGeometryCollection.setSrid(geometryCollection.getSRID());
        return pgGeometryCollection;
    }

    // to geolatte

    public static Geometry convert(org.postgis.Geometry geometry) {
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

    private static CoordinateReferenceSystem<?> getCrs(org.postgis.Geometry geom) {
        if (CrsRegistry.hasCoordinateReferenceSystemForEPSG(geom.getSrid())) {
            return CrsRegistry.getCoordinateReferenceSystemForEPSG(geom.getSrid(), null);
        }

        if (geom.dimension == 3) {
            return geom.haveMeasure
                    ? CoordinateReferenceSystems.PROJECTED_3DM_METER : CoordinateReferenceSystems.PROJECTED_3D_METER;
        }

        return geom.haveMeasure
                ? CoordinateReferenceSystems.PROJECTED_2DM_METER : CoordinateReferenceSystems.PROJECTED_2D_METER;
    }

    private static Point convert(org.postgis.Point geometry) {
        final CoordinateReferenceSystem<?> crs = getCrs(geometry);
        final Position pos = toPosition(geometry, crs.getPositionClass());

        return new Point(pos, crs);
    }

    private static <P extends Position> P toPosition(org.postgis.Point geometry, Class<P> pClass) {
        int d = geometry.dimension;
        double[] point = new double[d + (geometry.haveMeasure ? 1 : 0)];
        int offset = 0;
        point[offset++] = geometry.x;
        point[offset++] = geometry.y;
        if (d == 3) {
             point[offset++] = geometry.z;
        }
        if (geometry.haveMeasure) {
            point[offset++] = geometry.m;
        }

        return Positions.mkPosition(pClass, point);
    }

    private static PositionSequence convertPoints(org.postgis.Point[] points) {
        Class<? extends Position> pClass = getCrs(points[0]).getPositionClass();
        PositionSequenceBuilder pointSequence = PositionSequenceBuilders.fixedSized(points.length, pClass);
        for (int i = 0; i < points.length; i++) {
            pointSequence.add(toPosition(points[i], pClass));
        }
        return pointSequence.toPositionSequence();
    }

    private static GeometryCollection convert(org.postgis.GeometryCollection geometry) {
        Geometry[] geometries = new Geometry[geometry.numGeoms()];
        for (int i = 0; i < geometries.length; i++) {
            geometries[i] = convert(geometry.getSubGeometry(i));
        }
        return new GeometryCollection(geometries);
    }

    private static MultiPolygon convert(org.postgis.MultiPolygon geometry) {
        Polygon[] polygons = new Polygon[geometry.numPolygons()];
        for (int i = 0; i < polygons.length; i++) {
            polygons[i] = convert(geometry.getPolygon(i));
        }
        return new MultiPolygon(polygons);
    }

    private static MultiPoint convert(org.postgis.MultiPoint geometry) {
        Point[] points = new Point[geometry.numPoints()];
        for (int i = 0; i < points.length; i++) {
            points[i] = convert(geometry.getPoint(i));
        }
        return new MultiPoint(points);
    }

    private static MultiLineString convert(org.postgis.MultiLineString geometry) {
        LineString[] lineStrings = new LineString[geometry.numLines()];
        for (int i = 0; i < lineStrings.length; i++) {
            lineStrings[i] = convert(geometry.getLine(i));
        }
        return new MultiLineString(lineStrings);
    }

    private static Polygon convert(org.postgis.Polygon geometry) {
        LinearRing[] rings = new LinearRing[geometry.numRings()];
        for (int i = 0; i < rings.length; i++) {
            rings[i] = convert(geometry.getRing(i));
        }
        return new Polygon(rings);
    }

    private static LinearRing convert(org.postgis.LinearRing geometry) {
        if (geometry.isEmpty()) {
            return new LinearRing(getCrs(geometry));
        }
        return new LinearRing(convertPoints(geometry.getPoints()), getCrs(geometry));
    }


    private static LineString convert(org.postgis.LineString geometry) {
        if (geometry.isEmpty()) {
            return new LineString(getCrs(geometry));
        }
        PositionSequence points = convertPoints(geometry.getPoints());
        return new LineString(points, getCrs(geometry));
    }

    private PGgeometryConverter() { }

}
