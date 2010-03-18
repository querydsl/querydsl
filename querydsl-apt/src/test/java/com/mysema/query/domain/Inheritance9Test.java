package com.mysema.query.domain;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.mysema.query.annotations.QueryEntity;

/**
 * related to https://bugs.launchpad.net/querydsl/+bug/538148
 * 
 * @author tiwe
 *
 */
public class Inheritance9Test {
    
    @QueryEntity
    public static class Supertype extends SuperSupertype{ 
        
    }
    
    @QueryEntity
    public static class Entity1 extends Supertype{ 
        
    }
    
    @QueryEntity
    public static class Entity2 extends Supertype{ 
        
    }
    
    @Test
    public void test(){
        assertNotNull(QInheritance9Test_Entity1.entity1.id);
        assertNotNull(QInheritance9Test_Entity1.entity1.version);        
        assertNotNull(QInheritance9Test_Entity2.entity2.id);
        assertNotNull(QInheritance9Test_Entity2.entity2.version);
    }

}
