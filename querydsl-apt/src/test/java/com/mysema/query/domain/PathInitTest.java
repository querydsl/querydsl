package com.mysema.query.domain;

import static org.junit.Assert.*;

import org.junit.Test;

import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.annotations.QueryInit;

public class PathInitTest {

    @QueryEntity
    public static class E1{

        @QueryInit("e3.e4")
        public E2 e2;
        
        @QueryInit({"e3.*", "e33.e4"})
        public E2 e22;
        
        @QueryInit("*")
        public E2 e222;
    }
    
    @QueryEntity
    public static class E2{
        
        public E3 e3;
        
        public E3 e33;
    }

    @QueryEntity
    public static class E3{
    
        public E4 e4;
        
        public E4 e44;
    }

    @QueryEntity
    public static class E4{
    
        public E1 e1;
        
        public E1 e11;
    }
    
    @Test
    public void test(){
        // e2
        assertNotNull(QE1.e1.e2);
        assertNotNull(QE1.e1.e2.e3.e4);
        assertNull(QE1.e1.e2.e33);
        assertNull(QE1.e1.e2.e3.e44);
        
        // e22
        assertNotNull(QE1.e1.e22.e33.e4);
        assertNull(QE1.e1.e22.e33.e44);
    }
    
}
