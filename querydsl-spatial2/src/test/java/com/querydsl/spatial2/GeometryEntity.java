package com.querydsl.spatial2;

import com.querydsl.core.annotations.QueryEntity;
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
}
