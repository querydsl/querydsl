package com.mysema.query.domain;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.annotations.QuerySupertype;

public class EntityTest {
    
    @QueryEntity
    public static class Entity1 {
        String entity1Field;
    }
    
    @QueryEntity
    public static class Entity2 extends Supertype{        
        String entity2Field;
    }
    
    @QueryEntity
    public static class Entity3 extends Entity2{
        String entity3Field;
    }
    
    @QueryEntity
    public static class Entity4 extends Supertype2{

    }
    
    @QuerySupertype
    public static class Supertype {
        String supertypeField;        
    }
    
    @QuerySupertype
    public static class Supertype2 extends Supertype{

    }

    @SuppressWarnings("unchecked")
    @Test
    public void test(){
        assertTrue(QEntity2.entity2 instanceof QSupertype);
        assertTrue(QEntity3.entity3 instanceof QSupertype);
    }
}
