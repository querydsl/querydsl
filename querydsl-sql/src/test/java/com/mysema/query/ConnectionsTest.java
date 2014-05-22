package com.mysema.query;

import static org.junit.Assert.assertNotNull;

import org.geolatte.geom.codec.Wkt;
import org.junit.Test;

public class ConnectionsTest {

    @Test
    public void Valid_Wkt() {
        for (String wkt : Connections.getSpatialData().values()) {
            assertNotNull(Wkt.newWktDecoder(Wkt.Dialect.POSTGIS_EWKT_1).decode(wkt));
        }
    }

}
