package com.querydsl.sql.spatial;

import com.querydsl.sql.SQLTemplates;
import org.junit.Test;

public class MySQLSpatialTemplatesTest {

    @Test
    public void test() {
        // insert into SHAPES values (1, GeomFromText('POINT(2 2)'))
        SQLTemplates templates = new MySQLSpatialTemplates();
        //assertEquals("GeomFromText('POINT(2 2)')", templates.asLiteral(Wkt.fromWkt("Point(2 2)")));
    }

}
