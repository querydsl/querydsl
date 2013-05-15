package com.mysema.query.domain;

import org.junit.Test;

import com.mysema.query.annotations.QueryProjection;

public class Array2Test {
    
    public class Example {
    
        byte[] imageData;

        @QueryProjection    
        public Example(byte[] imageData) {
            this.imageData = imageData;
        }
    }

    @Test
    public void test() {
        
    }

}
