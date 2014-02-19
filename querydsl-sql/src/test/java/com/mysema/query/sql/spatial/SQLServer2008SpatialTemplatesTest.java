package com.mysema.query.sql.spatial;

import static org.junit.Assert.assertEquals;

import org.geolatte.geom.codec.Wkt;
import org.junit.Test;

import com.mysema.query.sql.SQLTemplates;

public class SQLServer2008SpatialTemplatesTest {

    @Test
    public void test() {
        // insert into SHAPES values (1, GeomFromText('POINT(2 2)'))
        SQLTemplates templates = new SQLServer2008SpatialTemplates();
        assertEquals("geometry::STGeomFromText('POINT(2 2)')", templates.asLiteral(Wkt.fromWkt("Point(2 2)")));
    }

}
