package com.mysema.query.jpa;

import java.lang.reflect.Field;

import org.junit.Test;

import com.mysema.query.types.OpsTest;

public class JPQLOpsTest {

    @Test
    public void Naming() throws IllegalAccessException {
        for (Field field : JPQLOps.class.getFields()) {
            OpsTest.validate(field);
        }
    }
}
