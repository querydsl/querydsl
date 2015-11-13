package com.mysema.query.spatial.hibernate;

import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.google.common.collect.Sets;
import com.mysema.query.spatial.SpatialOps;
import com.mysema.query.types.Operator;

public class HibernateSpatialSupportTest {

    @Test
    public void allMapped() throws IllegalAccessException {
        Set<Operator<?>> operators = Sets.newHashSet();
        for (Field field : SpatialOps.class.getDeclaredFields()) {
            if (field.getType().equals(Operator.class)) {
                operators.add((Operator<?>) field.get(null));
            }
        }
        Map<Operator<?>, String> mapping = HibernateSpatialSupport.getSpatialOps();
        for (Operator<?> operator : operators) {
            assertTrue(operator + " missing", mapping.containsKey(operator));
        }
    }
}
