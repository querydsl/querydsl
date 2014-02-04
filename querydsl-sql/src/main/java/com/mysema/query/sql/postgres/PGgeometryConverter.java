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
package com.mysema.query.sql.postgres;

import org.geolatte.geom.Geometry;
import org.geolatte.geom.GeometryCollection;
import org.geolatte.geom.LineString;
import org.geolatte.geom.LinearRing;
import org.geolatte.geom.MultiLineString;
import org.geolatte.geom.MultiPoint;
import org.geolatte.geom.MultiPolygon;
import org.geolatte.geom.Point;
import org.geolatte.geom.Points;
import org.geolatte.geom.PolyHedralSurface;
import org.geolatte.geom.Polygon;
import org.geolatte.geom.crs.CrsId;


/**
 * @author tiwe
 *
 */
public class PGgeometryConverter {

    // to postgis

    public static org.postgis.Geometry convert(Geometry geometry) {
        if (geometry instanceof Point) {
            return convert((Point)geometry);
        } else if (geometry instanceof LinearRing) {
            return convert((LinearRing)geometry);
        } else if (geometry instanceof LineString) {
            return convert((LineString)geometry);
        } else if (geometry instanceof MultiLineString) {
            return convert((MultiLineString)geometry);
        } else if (geometry instanceof Polygon) {
            return convert((Polygon)geometry);
        } else if (geometry instanceof PolyHedralSurface) {
            return convert((PolyHedralSurface)geometry);
        } else if (geometry instanceof MultiPoint) {
            return convert((MultiPoint)geometry);
        } else if (geometry instanceof MultiPolygon) {
            return convert((MultiPolygon)geometry);
        } else if (geometry instanceof GeometryCollection) {
            return convert((GeometryCollection)geometry);
        } else {
            throw new IllegalArgumentException(geometry.getClass().getName());
        }
    }

    private static org.postgis.Point convert(Point point) {
        org.postgis.Point pgPoint = new org.postgis.Point();
        pgPoint.srid = point.getSRID();
        pgPoint.dimension = point.getDimension();
        pgPoint.haveMeasure = false;
        pgPoint.x = point.getX();
        if (pgPoint.dimension > 1) {
            pgPoint.y = point.getY();
        }
        if (pgPoint.dimension > 2) {
            pgPoint.z = point.getZ();
        }
        if (point.isMeasured()) {
            pgPoint.m = point.getM();
            pgPoint.haveMeasure = true;
        }
        return pgPoint;
    }

    private static org.postgis.Point[] convertPoints(Geometry geometry) {
        org.postgis.Point[] pgPoints = new org.postgis.Point[geometry.getNumPoints()];
        for (int i = 0; i < pgPoints.length; i++) {
            pgPoints[i] = convert(geometry.getPointN(i));
        }
        return pgPoints;
    }

    private static org.postgis.LineString convert(LineString lineString) {
        org.postgis.Point[] pgPoints = convertPoints(lineString);
        org.postgis.LineString pgLineString = new org.postgis.LineString(pgPoints);
        pgLineString.haveMeasure = lineString.isMeasured();
        pgLineString.setSrid(lineString.getSRID());
        return pgLineString;
    }

    private static org.postgis.LinearRing convert(LinearRing linearRing) {
        org.postgis.Point[] pgPoints = convertPoints(linearRing);
        org.postgis.LinearRing pgLinearRing = new org.postgis.LinearRing(pgPoints);
        pgLinearRing.haveMeasure = linearRing.isMeasured();
        pgLinearRing.setSrid(linearRing.getSRID());
        return pgLinearRing;
    }

    private static org.postgis.MultiLineString convert(MultiLineString multiLineString) {
        org.postgis.LineString[] pgLineStrings = new org.postgis.LineString[multiLineString.getNumGeometries()];
        for (int i = 0; i < pgLineStrings.length; i++) {
            pgLineStrings[i] = convert(multiLineString.getGeometryN(i));
        }
        org.postgis.MultiLineString pgMultiLineString = new org.postgis.MultiLineString(pgLineStrings);
        pgMultiLineString.haveMeasure = multiLineString.isMeasured();
        pgMultiLineString.setSrid(multiLineString.getSRID());
        return pgMultiLineString;
    }

    private static org.postgis.Polygon convert(Polygon polygon) {
        int numRings = polygon.getNumInteriorRing();
        org.postgis.LinearRing[] rings = new org.postgis.LinearRing[numRings + 1];
        rings[0] = convert(polygon.getExteriorRing());
        for (int i = 0; i < numRings; i++) {
            rings[i+1] = convert(polygon.getInteriorRingN(i));
        }
        org.postgis.Polygon pgPolygon = new org.postgis.Polygon(rings);
        pgPolygon.setSrid(polygon.getSRID());
        return pgPolygon;
    }

    private static org.postgis.Polygon convert(PolyHedralSurface polyHedralSurface) {
        throw new UnsupportedOperationException();
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
            pgPolygons[i] = convert(multiPolygon.getGeometryN(i));
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
            return convert((org.postgis.Point)geometry);
        case org.postgis.Geometry.LINESTRING:
            return convert((org.postgis.LineString)geometry);
        case org.postgis.Geometry.LINEARRING:
            return convert((org.postgis.LinearRing)geometry);
        case org.postgis.Geometry.POLYGON:
            return convert((org.postgis.Polygon)geometry);
        case org.postgis.Geometry.MULTILINESTRING:
            return convert((org.postgis.MultiLineString)geometry);
        case org.postgis.Geometry.MULTIPOINT:
            return convert((org.postgis.MultiPoint)geometry);
        case org.postgis.Geometry.MULTIPOLYGON:
            return convert((org.postgis.MultiPolygon)geometry);
        case org.postgis.Geometry.GEOMETRYCOLLECTION:
            return convert((org.postgis.GeometryCollection)geometry);
        }
        throw new IllegalArgumentException(geometry.toString());
    }

    private static Point convert(org.postgis.Point geometry) {
        int d = geometry.dimension;
        CrsId srid = CrsId.valueOf(geometry.srid);
        if (!geometry.haveMeasure) {
            switch (d) {
            case 2:
                return Points.create(geometry.x, geometry.y, srid);
            case 3:
                return Points.create3D(geometry.x,  geometry.y, geometry.z, srid);
            }
        } else {
            switch (d) {
            case 2:
                return Points.createMeasured(geometry.x, geometry.y, geometry.m, srid);
            case 3:
                return Points.create(geometry.x,  geometry.y, geometry.z, geometry.m, srid);
            }
        }
        throw new IllegalArgumentException(geometry.toString());
    }

    private static GeometryCollection convert(org.postgis.GeometryCollection geometry) {

        // TODO Auto-generated method stub
        return null;
    }

    private static MultiPolygon convert(org.postgis.MultiPolygon geometry) {
        // TODO Auto-generated method stub
        return null;
    }

    private static MultiPoint convert(org.postgis.MultiPoint geometry) {
        // TODO Auto-generated method stub
        return null;
    }

    private static MultiLineString convert(org.postgis.MultiLineString geometry) {
        // TODO Auto-generated method stub
        return null;
    }

    private static Polygon convert(org.postgis.Polygon geometry) {
        // TODO Auto-generated method stub
        return null;
    }

    private static LinearRing convert(org.postgis.LinearRing geometry) {
        // TODO Auto-generated method stub
        return null;
    }

    private static LineString convert(org.postgis.LineString geometry) {
        // TODO Auto-generated method stub
        return null;
    }

}
