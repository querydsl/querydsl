package com.mysema.query.domain;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.annotations.QueryInit;
import com.mysema.query.annotations.QuerySupertype;

public class EntityTest extends AbstractTest{
    
    @QueryEntity
    public static class Entity1 {
        public String entity1Field;
        
        public Entity1 entity1Ref;
    }
    
    @QueryEntity
    public static class Entity2 extends Supertype{        
        public String entity2Field;
        
        public Entity2 entity2Ref;
    }
    
    @QueryEntity
    public static class Entity3 extends Entity2{
        public String entity3Field;
        
        @QueryInit("*")
        public Entity3 entity3Ref;
    }
    
    @QueryEntity
    public static class Entity4 extends Supertype2{

    }
    
    @QuerySupertype
    public static class Supertype {
        public String supertypeField;        
    }
    
    @QuerySupertype
    public static class Supertype2 extends Supertype{

    }

    @Test
    public void inheritance(){
        assertTrue(QSupertype.class.isAssignableFrom(QEntity2.class));
        assertTrue(QSupertype.class.isAssignableFrom(QEntity3.class));
    }
}
