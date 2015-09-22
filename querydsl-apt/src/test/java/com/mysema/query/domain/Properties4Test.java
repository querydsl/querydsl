package com.mysema.query.domain;

import static org.junit.Assert.assertEquals;

import javax.persistence.MappedSuperclass;

import org.junit.Test;

public class Properties4Test {

    @MappedSuperclass
    public abstract static class Naming {

        public abstract boolean is8FRecord();

    }

    @Test
    public void test() {
        assertEquals("8FRecord", QProperties4Test_Naming.naming._8FRecord.getMetadata().getName());
    }
}
