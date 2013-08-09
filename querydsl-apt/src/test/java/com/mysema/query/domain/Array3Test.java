package com.mysema.query.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.query.annotations.QueryEntity;

public class Array3Test {

    @QueryEntity
    public static class Domain {

        byte[] bytes;

        Byte[] bytes2;
    }

    @QueryEntity
    public static class Domain2 {

        byte[] bytes;
    }

    @QueryEntity
    public static class Domain3 {

        Byte[] bytes;
    }

    @Test
    public void Domain() {
        assertEquals(byte[].class, QArray3Test_Domain.domain.bytes.getType());
        assertEquals(Byte[].class, QArray3Test_Domain.domain.bytes2.getType());
    }

    @Test
    public void Domain2() {
        assertEquals(byte[].class, QArray3Test_Domain2.domain2.bytes.getType());
    }

    @Test
    public void Domain3() {
        assertEquals(Byte[].class, QArray3Test_Domain3.domain3.bytes.getType());
    }

}
