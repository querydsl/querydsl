package com.querydsl.spatial.jts;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import org.locationtech.jts.geom.*;

public class JTSGeometryPathTest {

    @Test
    public void convert() {
        JTSGeometryPath<Geometry> geometry = new JTSGeometryPath<Geometry>("geometry");
        assertEquals(new JTSGeometryCollectionPath<GeometryCollection>("geometry"), geometry.asCollection());
        assertEquals(new JTSLinearRingPath<LinearRing>("geometry"), geometry.asLinearRing());
        assertEquals(new JTSLineStringPath<LineString>("geometry"), geometry.asLineString());
        assertEquals(new JTSMultiLineStringPath<MultiLineString>("geometry"), geometry.asMultiLineString());
        assertEquals(new JTSMultiPointPath<MultiPoint>("geometry"), geometry.asMultiPoint());
        assertEquals(new JTSMultiPolygonPath<MultiPolygon>("geometry"), geometry.asMultiPolygon());
        assertEquals(new JTSPointPath<Point>("geometry"), geometry.asPoint());
        assertEquals(new JTSPolygonPath<Polygon>("geometry"), geometry.asPolygon());
    }
}
