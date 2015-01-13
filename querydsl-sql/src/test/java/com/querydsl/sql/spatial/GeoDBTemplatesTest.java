package com.querydsl.sql.spatial;

import com.querydsl.sql.SQLTemplates;
import org.junit.Test;

public class GeoDBTemplatesTest {

    @Test
    public void test() {
        // insert into SHAPES values (1, 'ST_GeomFromText('POINT(2 2)', 4326))
        SQLTemplates templates = new GeoDBTemplates();
        //assertEquals("ST_GeomFromText('POINT(2 2)')", templates.asLiteral(Wkt.fromWkt("Point(2 2)")));
    }

}
