package com.querydsl.sql;

import java.sql.Types;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ArrayTypesTest {

    private Configuration configuration;

    @Before
    public void setUp() {
        configuration = Configuration.DEFAULT;
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
