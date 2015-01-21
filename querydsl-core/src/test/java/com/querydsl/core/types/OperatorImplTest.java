package com.querydsl.core.types;

import static org.junit.Assert.assertEquals;
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

    @Test
    public void GetId() {
        assertEquals("com.querydsl.core.types.Ops#ALIAS", Ops.ALIAS.getId());
    }
}
