package com.mysema.query.lucene;

import java.lang.reflect.Field;

import org.junit.Test;

import com.mysema.query.types.OpsTest;

public class LuceneOpsTest {

    @Test
    public void Naming() throws IllegalAccessException {
        for (Field field : LuceneOps.class.getFields()) {
            OpsTest.validate(field);
        }
    }
}
