package com.mysema.query.domain;

import static org.junit.Assert.*;

import org.junit.Test;

import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.annotations.QueryInit;

public class QueryInitTest {

    private static final QPEntity e1 = QPEntity.pEntity;
    
    private static final QPEntity2 e2 = QPEntity2.pEntity2;
    
    @QueryEntity
    public static class PEntity{

        @QueryInit("e3.e4")
        public PEntity2 e2;
        
        @QueryInit({"e3.*", "e33.e4", "e333"})
        public PEntity2 e22;
        
        @QueryInit("*")
        public PEntity2 e222;
        
        public PEntity2 type;
        
        public PEntity2 inits;
    }
    
    @QueryEntity
    public static class PEntity2Super{
        
        public PEntity3 e333;
        
        @QueryInit("e4")
        public PEntity3 e3333;
    }
    
    @QueryEntity
    public static class PEntity2 extends PEntity2Super{
        
        public PEntity3 e3;
        
        public PEntity3 e33;
    }

    @QueryEntity
    public static class PEntity3{
    
        public PEntity4 e4;
        
        public PEntity4 e44;
    }

    @QueryEntity
    public static class PEntity4{
    
        public PEntity e1;
        
        public PEntity e11;
    }
    
    @Test
    public void basicInits(){
        // e2
        assertNotNull(e1.e2);
        assertNotNull(e1.e2.e3.e4);
        assertNull(e1.e2.e33);
        assertNull(e1.e2.e3.e44);
        
        // e22
        assertNotNull(e1.e22.e33.e4);
        assertNull(e1.e22.e33.e44);
        assertNotNull(e1.e22.e333);     
    }
    
    @Test
    public void deepSuperInits(){
        assertNotNull(e1.e22._super.e333); 
    }
    
    @Test
    public void rootSuperInits(){
        assertNotNull(e2.e3333.e4);
        assertNotNull(e2._super.e3333.e4);        
    }
    
    
}
