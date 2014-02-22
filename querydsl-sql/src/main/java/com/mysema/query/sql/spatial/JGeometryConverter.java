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
package com.mysema.query.sql.spatial;

import oracle.spatial.geometry.JGeometry;

import org.geolatte.geom.Geometry;
import org.geolatte.geom.GeometryCollection;
import org.geolatte.geom.LineString;
import org.geolatte.geom.LinearRing;
import org.geolatte.geom.MultiLineString;
import org.geolatte.geom.MultiPoint;
import org.geolatte.geom.MultiPolygon;
import org.geolatte.geom.Point;
import org.geolatte.geom.PointCollection;
import org.geolatte.geom.PolyHedralSurface;
import org.geolatte.geom.Polygon;

/**
 * @author tiwe
 *
 */
public class JGeometryConverter {

    // to jgeometry

    public static JGeometry convert(Geometry geometry) {
        switch (geometry.getGeometryType()) {
        case POINT: return convert((Point)geometry);
//        case CURVE:
//        case SURFACE:
        case GEOMETRY_COLLECTION: return convert((GeometryCollection)geometry);
        case LINE_STRING: return convert((LineString)geometry);
        case LINEAR_RING: return convert((LinearRing)geometry);
        case POLYGON: return convert((Polygon)geometry);
        case POLYHEDRAL_SURFACE: return convert((PolyHedralSurface)geometry);
//        case MULTI_SURFACE:
        case MULTI_POINT: return convert((MultiPoint)geometry);
//        case TIN
        case MULTI_POLYGON: return convert((MultiPolygon)geometry);
        case MULTI_LINE_STRING: return convert((MultiLineString)geometry);
        default: throw new IllegalArgumentException(geometry.toString());
        }
    }

    private static double[] getPoints(PointCollection points) {
        int dim = points.getCoordinateDimension();
        double[] values = new double[points.size() * dim];
        int offset = 0;
        for (int i = 0; i < points.size(); i++) {
            values[offset++] = points.getX(i);
            values[offset++] = points.getY(i);
            if (points.is3D()) {
                values[offset++] = points.getZ(i);
            }
            if (points.isMeasured()) {
                values[offset++] = points.getM(i);
            }
        }
        return values;
    }

    private static double[] getCoordinates(Point geometry) {
        double[] value = new double[geometry.getCoordinateDimension()];
        int offset = 0;
        value[offset++] = geometry.getX();
        value[offset++] = geometry.getY();
        if (geometry.is3D()) {
            value[offset++] = geometry.getZ();
        }
        if (geometry.isMeasured()) {
            value[offset++] = geometry.getM();
        }
        return value;
    }

    private static JGeometry convert(Polygon geometry) {
        int srid = geometry.getSRID();
        int dim = geometry.getCoordinateDimension();
        Object[] objects = new Object[geometry.getNumInteriorRing() + 1];
        objects[0] = getPoints(geometry.getExteriorRing().getPoints());
        for (int i = 0; i < geometry.getNumInteriorRing(); i++) {
            objects[i + 1] = getPoints(geometry.getInteriorRingN(i).getPoints());
        }
        if (geometry.isMeasured()) {
            return JGeometry.createLRSLinearPolygon(objects, dim - 1, srid);
        } else {
            return JGeometry.createLinearPolygon(objects, dim, srid);
        }
    }

    private static JGeometry convert(PolyHedralSurface geometry) {
        throw new UnsupportedOperationException();
    }

    private static JGeometry convert(LineString geometry) {
        int srid = geometry.getSRID();
        int dim = geometry.getCoordinateDimension();
        double[] points = getPoints(geometry.getPoints());
        if (geometry.isMeasured()) {
            return JGeometry.createLRSLinearLineString(points, dim - 1, srid);
        } else {
            return JGeometry.createLinearLineString(points, dim, srid);
        }
    }

    private static JGeometry convert(GeometryCollection geometry) {
        throw new UnsupportedOperationException();
    }

    private static JGeometry convert(MultiPoint geometry) {
        int srid = geometry.getSRID();
        int dim = geometry.getCoordinateDimension();
        Object[] objects = new Object[geometry.getNumPoints()];
        for (int i = 0; i < geometry.getNumPoints(); i++) {
            objects[i] = getCoordinates(geometry.getPointN(i));
        }
        if (geometry.isMeasured()) {
            throw new IllegalStateException("MultiPoint with measure not supported");
        } else {
            return JGeometry.createMultiPoint(objects, dim, srid);
        }
    }

    private static JGeometry convert(MultiPolygon geometry) {
        throw new UnsupportedOperationException();
    }

    private static JGeometry convert(MultiLineString geometry) {
        int srid = geometry.getSRID();
        int dim = geometry.getCoordinateDimension();
        Object[] objects = new Object[geometry.getNumGeometries()];
        for (int i = 0; i < geometry.getNumGeometries(); i++) {
            objects[i] = getPoints(geometry.getGeometryN(i).getPoints());
        }
        if (geometry.isMeasured()) {
            return JGeometry.createLRSLinearMultiLineString(objects, dim - 1, srid);
        } else {
            return JGeometry.createLinearMultiLineString(objects, dim, srid);
        }
    }

    private static JGeometry convert(Point geometry) {
        double[] value = getCoordinates(geometry);
        int srid = geometry.getSRID();
        if (geometry.isMeasured()) {
            return JGeometry.createLRSPoint(value, value.length - 1, srid);
        } else {
            return JGeometry.createPoint(value, value.length, srid);
        }
    }

    // to geoaltte

    public static Geometry convert(JGeometry geometry) {
        switch (geometry.getType()) {
        case JGeometry.GTYPE_COLLECTION: return convertCollection(geometry);
        case JGeometry.GTYPE_CURVE: return convertCurve(geometry);
        case JGeometry.GTYPE_MULTICURVE: return convertMultiCurve(geometry);
        case JGeometry.GTYPE_MULTIPOINT: return convertMultiPoint(geometry);
        case JGeometry.GTYPE_MULTIPOLYGON: return convertMultiPolygon(geometry);
        case JGeometry.GTYPE_POINT: return convertPoint(geometry);
        case JGeometry.GTYPE_POLYGON: return convertPolygon(geometry);
        default: throw new IllegalArgumentException(geometry.toString());
        }
    }

    private static Geometry convertPolygon(JGeometry geometry) {
        // TODO Auto-generated method stub
        return null;
    }

    private static Geometry convertPoint(JGeometry geometry) {
        // TODO Auto-generated method stub
        return null;
    }

    private static Geometry convertMultiPoint(JGeometry geometry) {
        // TODO Auto-generated method stub
        return null;
    }

    private static Geometry convertMultiPolygon(JGeometry geometry) {
        // TODO Auto-generated method stub
        return null;
    }

    private static Geometry convertMultiCurve(JGeometry geometry) {
        // TODO Auto-generated method stub
        return null;
    }

    private static Geometry convertCurve(JGeometry geometry) {
        // TODO Auto-generated method stub
        return null;
    }

    private static Geometry convertCollection(JGeometry geometry) {
        // TODO Auto-generated method stub
        return null;
    }

    private JGeometryConverter() {}

}
