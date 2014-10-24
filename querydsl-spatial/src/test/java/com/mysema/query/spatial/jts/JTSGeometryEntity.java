package com.mysema.query.spatial.jts;

import com.mysema.query.annotations.QueryEntity;
import com.vividsolutions.jts.geom.*;

@QueryEntity
public class JTSGeometryEntity {

    Geometry geometry;

    GeometryCollection geometryCollection;

    LinearRing linearRing;

    LineString lineString;

    MultiLineString multiLineString;

    MultiPoint multiPoint;

    MultiPolygon multiPolygon;

    Point point;

    Polygon polygon;

}
