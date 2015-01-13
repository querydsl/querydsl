package com.querydsl.spatial.jts.path;

import static org.junit.Assert.assertEquals;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import org.junit.Test;

public class JTSGeometryPathTest {

    @Test
    public void Convert() {
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
