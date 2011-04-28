package com.mysema.query.domain;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.mysema.query.annotations.PropertyType;
import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.annotations.QueryTransient;
import com.mysema.query.annotations.QueryType;

public class QueryTypeOverTransientTest {
    
    @QueryEntity
    public class Entity {
        
        @QueryType(PropertyType.ENTITY)
        @QueryTransient
        Entity reference;
        
    }
    
    @QueryEntity 
    public abstract class  Entity2 {
        
        @QueryType(PropertyType.ENTITY)
        @QueryTransient
        public abstract Entity getReference();
        
    }
    
    @Test
    public void Entity_Reference_Is_Available() {
        assertNotNull(QQueryTypeOverTransientTest_Entity.entity.reference);
    }
    
    @Test
    public void Entity2_Reference_Is_Available() {
        assertNotNull(QQueryTypeOverTransientTest_Entity2.entity2.reference);
    }

}
