package com.querydsl.spatial;

import static org.junit.Assert.assertEquals;

import org.geolatte.geom.*;
import org.junit.Test;

public class GeometryPathTest {

    @Test
    public void convert() {
        GeometryPath<Geometry> geometry = new GeometryPath<Geometry>("geometry");
        assertEquals(new GeometryCollectionPath<GeometryCollection>("geometry"), geometry.asCollection());
        assertEquals(new LinearRingPath<LinearRing>("geometry"), geometry.asLinearRing());
        assertEquals(new LineStringPath<LineString>("geometry"), geometry.asLineString());
        assertEquals(new MultiLineStringPath<MultiLineString>("geometry"), geometry.asMultiLineString());
        assertEquals(new MultiPointPath<MultiPoint>("geometry"), geometry.asMultiPoint());
        assertEquals(new MultiPolygonPath<MultiPolygon>("geometry"), geometry.asMultiPolygon());
        assertEquals(new PointPath<Point>("geometry"), geometry.asPoint());
        assertEquals(new PolygonPath<Polygon>("geometry"), geometry.asPolygon());
    }
}
