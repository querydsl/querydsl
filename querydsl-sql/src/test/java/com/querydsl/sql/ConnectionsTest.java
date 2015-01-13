package com.querydsl.sql;

import org.geolatte.geom.codec.Wkt;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class ConnectionsTest {

    @Test
    public void Valid_Wkt() {
        for (String wkt : Connections.getSpatialData().values()) {
            assertNotNull(Wkt.newDecoder(Wkt.Dialect.POSTGIS_EWKT_1).decode(wkt));
        }
    }

}
