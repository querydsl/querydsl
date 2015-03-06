package com.mysema.query.mongodb;

import java.lang.reflect.Field;

import org.junit.Test;

import com.mysema.query.types.OpsTest;

public class MongodbOpsTest {

    @Test
    public void Naming() throws IllegalAccessException {
        for (Field field : MongodbOps.class.getFields()) {
            OpsTest.validate(field);
        }
    }
}
