package com.querydsl.spatial.hibernate;

import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;

import com.querydsl.core.types.Operator;
import com.querydsl.spatial.SpatialOps;

public class HibernateSpatialSupportTest {

    @Test
    public void allMapped() {
        Map<Operator, String> mapping = HibernateSpatialSupport.getSpatialOps();
        for (Operator operator : SpatialOps.values()) {
            assertTrue(operator + " missing", mapping.containsKey(operator));
        }
    }
}
