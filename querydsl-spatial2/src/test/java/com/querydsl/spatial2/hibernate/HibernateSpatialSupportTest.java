package com.querydsl.spatial2.hibernate;

import com.querydsl.core.types.Operator;
import com.querydsl.spatial2.SpatialOps;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertTrue;

public class HibernateSpatialSupportTest {

    @Test
    public void allMapped() {
        Map<Operator, String> mapping = HibernateSpatialSupport.getSpatialOps();
        for (Operator operator : SpatialOps.values()) {
            assertTrue(operator + " missing", mapping.containsKey(operator));
        }
    }
}
