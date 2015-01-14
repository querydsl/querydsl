package com.querydsl.sql.spatial;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.geolatte.geom.Geometry;
import org.junit.Test;

public class SQLServerGeometryWriterTest extends AbstractConverterTest {

    @Test
    public void RoundTrip() throws IOException {
        for (Geometry geometry : getGeometries()) {
            byte[] bytes = new SQLServerGeometryWriter().write(geometry);
            Geometry geometry2 = new SQLServerGeometryReader().read(bytes);
            assertEquals(geometry, geometry2);
        }
    }

}
