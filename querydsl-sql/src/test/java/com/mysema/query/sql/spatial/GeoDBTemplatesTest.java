package com.mysema.query.sql.spatial;

import static org.junit.Assert.assertEquals;

import org.geolatte.geom.codec.Wkt;
import org.junit.Test;

import com.mysema.query.sql.SQLTemplates;

public class GeoDBTemplatesTest {

    @Test
    public void test() {
        // insert into SHAPES values (1, 'ST_GeomFromText('POINT(2 2)', 4326))
        SQLTemplates templates = new GeoDBTemplates();
        assertEquals("ST_GeomFromText('POINT(2 2)')", templates.asLiteral(Wkt.fromWkt("Point(2 2)")));
    }

}
