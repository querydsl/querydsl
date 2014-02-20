package com.mysema.query.sql.spatial;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Collection;

import org.geolatte.geom.Geometry;
import org.geolatte.geom.codec.Wkt;
import org.junit.Test;

import com.mysema.query.Connections;

public class SQLServerGeometryWriterTest {

    @Test
    public void RoundTrip() throws IOException {
        Collection<String> wkt = Connections.getSpatialData().values();
        for (String geoWkt : wkt) {
            System.err.println(geoWkt);
            Geometry geometry = Wkt.fromWkt(geoWkt);
            byte[] bytes = new SQLServerGeometryWriter().write(geometry);
            Geometry geometry2 = new SQLServerGeometryReader().read(bytes);
            assertEquals(geometry, geometry2);
        }
    }

}
