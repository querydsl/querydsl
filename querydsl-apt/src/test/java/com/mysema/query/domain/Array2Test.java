package com.mysema.query.domain;

import org.junit.Test;

import com.mysema.query.annotations.QueryProjection;
import com.mysema.query.types.path.SimplePath;

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
