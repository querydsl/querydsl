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

import static oracle.spatial.geometry.JGeometry.*;

import org.geolatte.geom.*;
import org.geolatte.geom.Point;
import org.geolatte.geom.crs.CoordinateReferenceSystem;

import oracle.spatial.geometry.JGeometry;
import org.geolatte.geom.crs.CoordinateReferenceSystems;
import org.geolatte.geom.crs.CrsRegistry;

final class JGeometryConverter {

    // to jgeometry

    public static JGeometry convert(Geometry geometry) {
        switch (geometry.getGeometryType()) {
        case POINT: return convert((Point) geometry);
//        case CURVE:
//        case SURFACE:
        case GEOMETRYCOLLECTION: return convert((GeometryCollection) geometry);
        case LINESTRING: return convert((LineString) geometry);
        case LINEARRING: return convert((LinearRing) geometry);
        case POLYGON: return convert((Polygon) geometry);
//        case MULTI_SURFACE:
        case MULTIPOINT: return convert((MultiPoint) geometry);
//        case TIN
        case MULTIPOLYGON: return convert((MultiPolygon) geometry);
        case MULTILINESTRING: return convert((MultiLineString) geometry);
        default: throw new IllegalArgumentException(geometry.toString());
        }
    }

    private static double[] getPoints(PositionSequence points) {
        int dim = points.getCoordinateDimension();
        double[] values = new double[points.size() * dim];
        int offset = 0;
        for (int i = 0; i < points.size(); i++) {
            final Position pos = points.getPositionN(i);
            values[offset++] = pos.getCoordinate(0);
            values[offset++] = pos.getCoordinate(1);
            if (points.getCoordinateDimension() > 2) {
                values[offset++] = pos.getCoordinate(2);
            }
            if (points.getCoordinateDimension() > 3) {
                values[offset++] = pos.getCoordinate(3);
            }
        }
        return values;
    }

    private static double[] getCoordinates(Point geometry) {
        double[] value = new double[geometry.getCoordinateDimension()];
        geometry.getPositions().getCoordinates(0, value);
        return value;
    }

    private static JGeometry convert(Polygon geometry) {
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
        int gtype = dim * 1000 + (geometry.getCoordinateDimension() > 3 ? dim : 0) * 100 + GTYPE_POLYGON;
        return new JGeometry(gtype, srid, elemInfo, points);
    }

    private static JGeometry convert(LineString geometry) {
        int srid = geometry.getSRID();
        int dim = geometry.getCoordinateDimension();
        double[] points = getPoints(geometry.getPositions());

        if (geometry.getPositions().getPositionFactory().hasMComponent()) {
            return JGeometry.createLRSLinearLineString(points, dim - 1, srid);
        } else {
            return JGeometry.createLinearLineString(points, dim, srid);
        }
    }

    private static JGeometry convert(GeometryCollection geometry) {
        // TODO
        throw new UnsupportedOperationException();
    }

    private static JGeometry convert(MultiPoint geometry) {
        int srid = geometry.getSRID();
        int dim = geometry.getCoordinateDimension();
        double[] points = getPoints(geometry.getPositions());
        int[] elemInfo = new int[]{1, 1, geometry.getNumPositions()};
        int gtype = dim * 1000 + (geometry.getCoordinateDimension() > 3 ? dim : 0) * 100 + GTYPE_MULTIPOINT;
        return new JGeometry(gtype, srid, elemInfo, points);
    }

    private static JGeometry convert(MultiPolygon geometry) {
        // TODO
        throw new UnsupportedOperationException();
    }

    private static JGeometry convert(MultiLineString geometry) {
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
        int gtype = dim * 1000 + (geometry.getCoordinateDimension() > 3 ? dim : 0) * 100 + GTYPE_MULTICURVE;
        return new JGeometry(gtype, srid, elemInfo, points);
    }

    private static JGeometry convert(Point geometry) {
        double[] value = getCoordinates(geometry);
        int srid = geometry.getSRID();
        if (geometry.getPositions().getPositionFactory().hasMComponent()) {
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

    private static PositionSequence getPoints(JGeometry geometry, CoordinateReferenceSystem<?> crs) {
        return toSequence(geometry.getOrdinatesArray(), crs);
    }

    private static PositionSequence toSequence(double[] ordinatesArray, CoordinateReferenceSystem crs) {
        if (ordinatesArray.length == 0) {
            return PositionSequenceBuilders.fixedSized(0, crs.getPositionClass()).toPositionSequence();
        }


        final int coordinateDimension = crs.getCoordinateDimension();
        final double[] c = new double[coordinateDimension];
        final PositionSequenceBuilder<?> b = PositionSequenceBuilders.variableSized(crs.getPositionClass());

        for (int i = 0; i < ordinatesArray.length; i += coordinateDimension) {
            System.arraycopy(ordinatesArray, i, c, 0, coordinateDimension);
            b.add(c);
        }

        return b.toPositionSequence();
    }

    private static Polygon convertPolygon(JGeometry geometry) {
        CoordinateReferenceSystem<?> crs = getCrs(geometry);
        Object[] elements = geometry.getOrdinatesOfElements();
        LinearRing[] rings = new LinearRing[elements.length];
        for (int i = 0; i < elements.length; i++) {
            final double[] ring = (double[]) elements[i];
            rings[i] = new LinearRing(toSequence(ring, crs), crs);
        }
        return new Polygon(rings);
    }

    private static CoordinateReferenceSystem<?> getCrs(JGeometry geometry) {
        if (CrsRegistry.hasCoordinateReferenceSystemForEPSG(geometry.getSRID())) {
            return CrsRegistry.getCoordinateReferenceSystemForEPSG(geometry.getSRID(), null);
        }

        if (geometry.isLRSGeometry()) {
            return geometry.getDimensions() > 3
                    ? CoordinateReferenceSystems.PROJECTED_3DM_METER
                    : CoordinateReferenceSystems.PROJECTED_2DM_METER;
        }

        return geometry.getDimensions() > 2
                ? CoordinateReferenceSystems.PROJECTED_3D_METER
                : CoordinateReferenceSystems.PROJECTED_2D_METER;
    }

    private static Point convertPoint(JGeometry geometry) {
        final CoordinateReferenceSystem<? extends Position> crs = getCrs(geometry);
        final Position pos = Positions.mkPosition(crs, geometry.getPoint());

        return new Point(pos, crs);
    }

    private static LineString convertCurve(JGeometry geometry) {
        final CoordinateReferenceSystem<?> crs = getCrs(geometry);
        return new LineString(getPoints(geometry, crs), crs);
    }

    private static MultiPoint convertMultiPoint(JGeometry geometry) {
        final CoordinateReferenceSystem<? extends Position> crs = getCrs(geometry);
        JGeometry[] elements = geometry.getElements();
        if (elements == null || elements.length == 0) {
            return Geometries.mkEmptyMultiPoint(crs);
        }
        Point[] points = new Point[elements.length];
        int dimensions = geometry.getDimensions();
        double[] ordinates = geometry.getOrdinatesArray();

        int offset = 0;
        for (int i = 0; i < points.length; i++) {
            double[] coords = new double[dimensions];
            System.arraycopy(ordinates, offset, coords, 0, coords.length);
            points[i] = new Point(Positions.mkPosition(crs, coords), crs);
            offset += dimensions;
        }
        return new MultiPoint(points);
    }

    // FIXME
    private static MultiPolygon convertMultiPolygon(JGeometry geometry) {
        JGeometry[] elements = geometry.getElements();
        if (elements == null || elements.length == 0) {
            return Geometries.mkEmptyMultiPolygon(getCrs(geometry));
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
        CoordinateReferenceSystem<? extends Position> crs = getCrs(geometry);
        if (elements == null || elements.length == 0) {
            return Geometries.mkEmptyMultiLineString(crs);
        }
        LineString[] lineStrings = new LineString[elements.length];
        for (int i = 0; i < elements.length; i++) {
            lineStrings[i] = new LineString(getPoints(elements[i], crs), crs);
        }
        return new MultiLineString(lineStrings);
    }

    // FIXME
    private static GeometryCollection convertCollection(JGeometry geometry) {
        JGeometry[] elements = geometry.getElements();
        if (elements == null || elements.length == 0) {
            return Geometries.mkEmptyGeometryCollection(getCrs(geometry));
        }
        Geometry[] geometries = new Geometry[elements.length];
        for (int i = 0; i < elements.length; i++) {
            geometries[i] = convert(elements[i]);
        }
        return new GeometryCollection(geometries);
    }

    private JGeometryConverter() { }

}
