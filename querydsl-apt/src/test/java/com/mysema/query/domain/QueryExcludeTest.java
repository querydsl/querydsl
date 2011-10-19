package com.mysema.query.domain;

import static org.junit.Assert.*;

import org.junit.Test;

import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.annotations.QueryExclude;
import com.mysema.query.types.path.EntityPathBase;

public class QueryExcludeTest {
    
    @QueryExclude
    @QueryEntity
    public class Entity {
        
    }
    
    @QueryEntity
    public class SubEntity extends Entity {
        
    }
    
    @Test
    public void SubEntity() {
        assertEquals(EntityPathBase.class, QQueryExcludeTest_SubEntity.class.getSuperclass());
    }
    

}
