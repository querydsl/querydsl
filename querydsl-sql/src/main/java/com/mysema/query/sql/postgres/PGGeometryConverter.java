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
import org.geolatte.geom.MultiLineString;
import org.geolatte.geom.MultiPoint;
import org.geolatte.geom.MultiPolygon;
import org.geolatte.geom.Point;
import org.geolatte.geom.Points;
import org.geolatte.geom.PolyHedralSurface;
import org.geolatte.geom.Polygon;
import org.geolatte.geom.crs.CrsId;
import org.postgis.LinearRing;
import org.postgis.PGgeometry;


/**
 * @author tiwe
 *
 */
public class PGGeometryConverter {

    public static PGgeometry convert(Geometry geometry) {
        if (geometry instanceof Point) {
            return convert((Point)geometry);
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

    private static PGgeometry convert(Point point) {
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
        return new PGgeometry(pgPoint);
    }

    private static PGgeometry convert(LineString lineString) {
        return null; // TODO
    }

    private static PGgeometry convert(MultiLineString multiLineString) {
        return null; // TODO
    }

    private static PGgeometry convert(Polygon polygon) {
        return null; // TODO
    }

    private static PGgeometry convert(PolyHedralSurface polyHedralSurface) {
        return null; // TODO
    }

    private static PGgeometry convert(MultiPoint multiPoint) {
        return null; // TODO
    }

    private static PGgeometry convert(MultiPolygon multiPolygon) {
        return null; // TODO
    }

    private static PGgeometry convert(GeometryCollection multiPolygon) {
        return null; // TODO
    }

    public static Geometry convert(PGgeometry geometry) {
        switch (geometry.getGeoType()) {
        case org.postgis.Geometry.POINT:
            return convert((org.postgis.Point)geometry.getGeometry());
        case org.postgis.Geometry.LINESTRING:
            return convert((org.postgis.LineString)geometry.getGeometry());
        case org.postgis.Geometry.LINEARRING:
            return convert((org.postgis.LinearRing)geometry.getGeometry());
        case org.postgis.Geometry.POLYGON:
            return convert((org.postgis.Polygon)geometry.getGeometry());
        case org.postgis.Geometry.MULTILINESTRING:
            return convert((org.postgis.MultiLineString)geometry.getGeometry());
        case org.postgis.Geometry.MULTIPOINT:
            return convert((org.postgis.MultiPoint)geometry.getGeometry());
        case org.postgis.Geometry.MULTIPOLYGON:
            return convert((org.postgis.MultiPolygon)geometry.getGeometry());
        case org.postgis.Geometry.GEOMETRYCOLLECTION:
            return convert((org.postgis.GeometryCollection)geometry.getGeometry());
        }
        throw new IllegalArgumentException(geometry.toString());
    }

    private static Geometry convert(org.postgis.GeometryCollection geometry) {
        // TODO Auto-generated method stub
        return null;
    }

    private static Geometry convert(org.postgis.MultiPolygon geometry) {
        // TODO Auto-generated method stub
        return null;
    }

    private static Geometry convert(org.postgis.MultiPoint geometry) {
        // TODO Auto-generated method stub
        return null;
    }

    private static Geometry convert(org.postgis.MultiLineString geometry) {
        // TODO Auto-generated method stub
        return null;
    }

    private static Geometry convert(org.postgis.Polygon geometry) {
        // TODO Auto-generated method stub
        return null;
    }

    private static Geometry convert(LinearRing geometry) {
        // TODO Auto-generated method stub
        return null;
    }

    private static Geometry convert(org.postgis.LineString geometry) {
        // TODO Auto-generated method stub
        return null;
    }

    private static Geometry convert(org.postgis.Point geometry) {
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

}
