package com.querydsl.apt.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Test;

import com.querydsl.core.annotations.QueryEntity;
import com.querydsl.apt.domain.QArray3Test_Domain;
import com.querydsl.apt.domain.QArray3Test_Domain2;
import com.querydsl.apt.domain.QArray3Test_Domain3;

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
        Assert.assertEquals(byte[].class, QArray3Test_Domain.domain.bytes.getType());
        assertEquals(Byte[].class, QArray3Test_Domain.domain.bytes2.getType());
    }

    @Test
    public void Domain2() {
        Assert.assertEquals(byte[].class, QArray3Test_Domain2.domain2.bytes.getType());
    }

    @Test
    public void Domain3() {
        Assert.assertEquals(Byte[].class, QArray3Test_Domain3.domain3.bytes.getType());
    }

}
