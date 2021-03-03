package com.querydsl.spatial;

import org.geolatte.geom.*;

import com.querydsl.core.annotations.QueryEntity;

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
