package com.mysema.query.sql;

import static org.junit.Assert.*;

import java.sql.Types;

import org.junit.Test;

public class JDBCTypeMappingTest {

    @Test
    public void testGet() {
        JDBCTypeMapping typeMapping = new JDBCTypeMapping();
        assertEquals(Float.class, typeMapping.get(Types.FLOAT));
        assertEquals(Float.class, typeMapping.get(Types.REAL));
    }

}
