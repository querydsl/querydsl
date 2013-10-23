package com.mysema.query.types;

import static org.junit.Assert.assertFalse;

import java.lang.reflect.Field;
import java.util.Map;

import org.junit.Test;

public class OperatorImplTest {

    @Test
    public void Cache_Isnt_Empty() throws Exception {
        Field field = OperatorImpl.class.getDeclaredField("OPS");
        field.setAccessible(true);
        Map map = (Map) field.get(null);
        assertFalse(map.isEmpty());
    }
}
