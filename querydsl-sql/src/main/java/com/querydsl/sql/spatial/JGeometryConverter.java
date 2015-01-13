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

import oracle.spatial.geometry.JGeometry;
import org.geolatte.geom.*;
import org.geolatte.geom.Point;
import org.geolatte.geom.crs.CrsId;
import static oracle.spatial.geometry.JGeometry.*;

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
        double[] points = getPoints(geometry.getPoints());
        int[] elemInfo = new int[3 + geometry.getNumInteriorRing() * 3];
        int offset = 0;
        int pointOffset = 1;
        elemInfo[offset++] = pointOffset;
        elemInfo[offset++] = 1003; // exterior
        elemInfo[offset++] = 1;
        pointOffset += geometry.getExteriorRing().getNumPoints() * dim;
        for (int i = 0; i < geometry.getNumInteriorRing(); i++) {
            elemInfo[offset++] = pointOffset;
            elemInfo[offset++] = 2003;
            elemInfo[offset++] = 1;
            pointOffset += geometry.getInteriorRingN(i).getNumPoints() * dim;
        }
        int gtype = dim * 1000 + (geometry.isMeasured() ? dim : 0) * 100 + GTYPE_POLYGON;
        return new JGeometry(gtype, srid, elemInfo, points);
    }

    private static JGeometry convert(PolyHedralSurface geometry) {
        throw new UnsupportedOperationException();
    }

    private static JGeometry convert(LineString geometry) {
        int srid = geometry.getSRID();
        int dim = geometry.getCoordinateDimension();
        double[] points = getPoints(geometry.getPoints());
        int[] elemInfo = new int[]{1, 2, 1};
        int gtype = dim * 1000 + (geometry.isMeasured() ? dim : 0) * 100 + GTYPE_CURVE;
        return new JGeometry(gtype, srid, elemInfo, points);
    }

    private static JGeometry convert(GeometryCollection geometry) {
        // TODO
        throw new UnsupportedOperationException();
    }

    private static JGeometry convert(MultiPoint geometry) {
        int srid = geometry.getSRID();
        int dim = geometry.getCoordinateDimension();
        double[] points = getPoints(geometry.getPoints());
        int[] elemInfo = new int[]{1, 1, geometry.getNumPoints()};
        int gtype = dim * 1000 + (geometry.isMeasured() ? dim : 0) * 100 + GTYPE_MULTIPOINT;
        return new JGeometry(gtype, srid, elemInfo, points);
    }

    private static JGeometry convert(MultiPolygon geometry) {
        // TODO
        throw new UnsupportedOperationException();
    }

    private static JGeometry convert(MultiLineString geometry) {
        int srid = geometry.getSRID();
        int dim = geometry.getCoordinateDimension();
        double[] points = getPoints(geometry.getPoints());
        int[] elemInfo = new int[geometry.getNumGeometries() * 3];
        int offset = 0;
        int pointOffset = 1;
        for (int i = 0; i < geometry.getNumGeometries(); i++) {
            elemInfo[offset++] = pointOffset;
            elemInfo[offset++] = 2;
            elemInfo[offset++] = 1;
            pointOffset += geometry.getGeometryN(i).getNumPoints() * dim;
        }
        int gtype = dim * 1000 + (geometry.isMeasured() ? dim : 0) * 100 + GTYPE_MULTICURVE;
        return new JGeometry(gtype, srid, elemInfo, points);
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

    // to geolatte

    public static Geometry convert(JGeometry geometry) {
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

    private static PointSequence getPoints(JGeometry geometry) {
        CrsId crs = CrsId.valueOf(geometry.getSRID());
        int dimensions = geometry.getDimensions();
        boolean measured = geometry.isLRSGeometry();
        DimensionalFlag flag = DimensionalFlag.valueOf(dimensions > (measured ? 3 : 2), measured);
        double[] ordinates = geometry.getOrdinatesArray();
        return PointCollectionFactory.create(ordinates, flag, crs);
    }

    private static Polygon convertPolygon(JGeometry geometry) {
        CrsId crs = CrsId.valueOf(geometry.getSRID());
        int dimensions = geometry.getDimensions();
        boolean measured = geometry.isLRSGeometry();
        DimensionalFlag flag = DimensionalFlag.valueOf(dimensions > (measured ? 3 : 2), measured);
        Object[] elements = geometry.getOrdinatesOfElements();
        LinearRing[] rings = new LinearRing[elements.length];
        for (int i = 0; i < elements.length; i++) {
            PointSequence points = PointCollectionFactory.create((double[]) elements[i], flag, crs);
            rings[i] = new LinearRing(points);
        }
        return new Polygon(rings);
    }

    private static Point convertPoint(JGeometry geometry) {
        CrsId crs = CrsId.valueOf(geometry.getSRID());
        double[] point = geometry.getPoint();
        int dimensions = geometry.getDimensions();
        boolean measured = geometry.isLRSGeometry();
        DimensionalFlag flag = DimensionalFlag.valueOf(dimensions > (measured ? 3 : 2), measured);
        return new Point(PointCollectionFactory.create(point, flag, crs));
    }

    private static LineString convertCurve(JGeometry geometry) {
        CrsId crs = CrsId.valueOf(geometry.getSRID());
        PointSequence points = getPoints(geometry);
        return new LineString(points);
    }

    private static MultiPoint convertMultiPoint(JGeometry geometry) {
        CrsId crs = CrsId.valueOf(geometry.getSRID());
        JGeometry[] elements = geometry.getElements();
        if (elements == null || elements.length == 0) {
            return MultiPoint.createEmpty();
        }
        Point[] points = new Point[elements.length];
        int dimensions = geometry.getDimensions();
        double[] ordinates = geometry.getOrdinatesArray();
        boolean measured = geometry.isLRSGeometry();
        DimensionalFlag flag = DimensionalFlag.valueOf(dimensions > (measured ? 3 : 2), measured);
        int offset = 0;
        for (int i = 0; i < points.length; i++) {
            double[] coords = new double[dimensions];
            System.arraycopy(ordinates, offset, coords, 0, coords.length);
            points[i] = new Point(PointCollectionFactory.create(coords, flag, crs));
            offset += dimensions;
        }
        return new MultiPoint(points);
    }

    // FIXME
    private static MultiPolygon convertMultiPolygon(JGeometry geometry) {
        JGeometry[] elements = geometry.getElements();
        if (elements == null || elements.length == 0) {
            return MultiPolygon.createEmpty();
        }
        Polygon[] polygons = new Polygon[elements.length];
        for (int i = 0; i < elements.length; i++) {
            polygons[i] = convertPolygon(elements[i]);
        }
        return new MultiPolygon(polygons);
    }

    // FIXME
    private static MultiLineString convertMultiCurve(JGeometry geometry) {
        JGeometry[] elements = geometry.getElements();
        if (elements == null || elements.length == 0) {
            return MultiLineString.createEmpty();
        }
        CrsId crs = CrsId.valueOf(geometry.getSRID());
        LineString[] lineStrings = new LineString[elements.length];
        for (int i = 0; i < elements.length; i++) {
            PointSequence points = getPoints(elements[i]);
            lineStrings[i] = new LineString(points);
        }
        return new MultiLineString(lineStrings);
    }

    // FIXME
    private static GeometryCollection convertCollection(JGeometry geometry) {
        JGeometry[] elements = geometry.getElements();
        if (elements == null || elements.length == 0) {
            return GeometryCollection.createEmpty();
        }
        Geometry[] geometries = new Geometry[elements.length];
        for (int i = 0; i < elements.length; i++) {
            geometries[i] = convert(elements[i]);
        }
        return new GeometryCollection(geometries);
    }

    private JGeometryConverter() {}

}
