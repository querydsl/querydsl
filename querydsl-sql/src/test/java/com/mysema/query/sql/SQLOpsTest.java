package com.mysema.query.sql;

import java.lang.reflect.Field;

import org.junit.Test;

import com.mysema.query.types.OpsTest;

public class SQLOpsTest {

    @Test
    public void Naming() throws IllegalAccessException {
        for (Field field : SQLOps.class.getFields()) {
            OpsTest.validate(field);
        }
    }
}
