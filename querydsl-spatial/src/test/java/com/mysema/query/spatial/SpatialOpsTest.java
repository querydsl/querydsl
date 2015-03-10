package com.mysema.query.spatial;

import java.lang.reflect.Field;

import org.junit.Test;

import com.mysema.query.types.OpsTest;

public class SpatialOpsTest {

    @Test
    public void Naming() throws IllegalAccessException {
        for (Field field : SpatialOps.class.getFields()) {
            OpsTest.validate(field);
        }
    }
}
