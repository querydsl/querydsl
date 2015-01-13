package com.querydsl.sql.spatial;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.geolatte.geom.Geometry;
import org.junit.Test;

public class PGgeometryConverterTest extends AbstractConverterTest {

    @Test
    public void RoundTrip() {
        List<Geometry> geometries = getGeometries();
        for (Geometry geometry : geometries) {
            org.postgis.Geometry converted = PGgeometryConverter.convert(geometry);
            Geometry back = PGgeometryConverter.convert(converted);
            assertEquals(geometry, back);
        }
    }

}
