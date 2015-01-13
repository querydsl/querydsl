package com.querydsl.apt.domain;

import org.junit.Test;

import com.querydsl.core.annotations.QueryProjection;
import com.querydsl.apt.domain.QArray2Test_Example;
import com.querydsl.core.types.path.SimplePath;

public class Array2Test {

    public static class Example {

        byte[] imageData;

        @QueryProjection
        public Example(byte[] param0) {
            this.imageData = param0;
        }
    }

    @Test
    public void test() {
        new QArray2Test_Example(new SimplePath<byte[]>(byte[].class, "bytes")).newInstance(new byte[0]);
    }

}
