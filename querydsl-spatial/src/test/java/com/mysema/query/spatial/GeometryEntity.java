package com.mysema.query.spatial;

import com.mysema.query.annotations.QueryEntity;
import org.geolatte.geom.*;

@QueryEntity
public class GeometryEntity {

    Geometry geometry;

    GeometryCollection geometryCollection;

    LinearRing linearRing;

    LineString lineString;

    MultiLineString multiLineString;

    MultiPoint multiPoint;

    MultiPolygon multiPolygon;

    Point point;

    Polygon polygon;

    PolyHedralSurface polyHedralSurface;
}
