package com.querydsl.spatial.path;

import static org.junit.Assert.assertEquals;

import org.geolatte.geom.Geometry;
import org.geolatte.geom.GeometryCollection;
import org.geolatte.geom.LineString;
import org.geolatte.geom.LinearRing;
import org.geolatte.geom.MultiLineString;
import org.geolatte.geom.MultiPoint;
import org.geolatte.geom.MultiPolygon;
import org.geolatte.geom.Point;
import org.geolatte.geom.PolyHedralSurface;
import org.geolatte.geom.Polygon;
import org.junit.Test;

public class GeometryPathTest {

    @Test
    public void Convert() {
        GeometryPath<Geometry> geometry = new GeometryPath<Geometry>("geometry");
        assertEquals(new GeometryCollectionPath<GeometryCollection>("geometry"), geometry.asCollection());
        assertEquals(new LinearRingPath<LinearRing>("geometry"), geometry.asLinearRing());
        assertEquals(new LineStringPath<LineString>("geometry"), geometry.asLineString());
        assertEquals(new MultiLineStringPath<MultiLineString>("geometry"), geometry.asMultiLineString());
        assertEquals(new MultiPointPath<MultiPoint>("geometry"), geometry.asMultiPoint());
        assertEquals(new MultiPolygonPath<MultiPolygon>("geometry"), geometry.asMultiPolygon());
        assertEquals(new PointPath<Point>("geometry"), geometry.asPoint());
        assertEquals(new PolygonPath<Polygon>("geometry"), geometry.asPolygon());
        assertEquals(new PolyhedralSurfacePath<PolyHedralSurface>("geometry"), geometry.asPolygHedralSurface());
    }
}
