package com.mysema.query.domain;

import org.junit.Test;

import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.annotations.QueryProjection;
import com.mysema.query.types.expr.EString;

public class QueryProjectionTest {

    @QueryEntity
    public static class EntityWithProjection{
        
        public EntityWithProjection(long id){
            
        }
        
        @QueryProjection
        public EntityWithProjection(String name){
            
        }
    }
    
    public static class DTOWithProjection {
        
        public DTOWithProjection(long id){
            
        }
        
        @QueryProjection
        public DTOWithProjection(String name){
            
        }
    }
    
    @Test
    public void test() throws SecurityException, NoSuchMethodException{
        QEntityWithProjection.create(EString.create(""));
        new QDTOWithProjection(EString.create(""));
    }
}
