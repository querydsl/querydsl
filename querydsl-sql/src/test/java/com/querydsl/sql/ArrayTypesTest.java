package com.querydsl.sql;

import static org.junit.Assert.assertEquals;

import java.sql.Types;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

public class ArrayTypesTest {

    private Configuration configuration;

    @Before
    public void setUp() {
        configuration = Configuration.DEFAULT;
        System.setProperty("user.timezone", "UTC");
        TimeZone.setDefault(null);
    }

    @Test
    public void test() {
        assertEquals(Integer[].class, getJavaType("_integer"));
        assertEquals(Integer[].class, getJavaType("integer[]"));
        assertEquals(Integer[].class, getJavaType("INTEGER ARRAY"));
    }

    private Class<?> getJavaType(String typeName) {
        return configuration.getJavaType(Types.ARRAY, typeName, 0, 0, "", "");
    }
}
