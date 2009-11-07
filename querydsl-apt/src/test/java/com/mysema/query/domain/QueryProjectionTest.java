package com.mysema.query.domain;

import javax.jdo.annotations.PersistenceCapable;
import javax.persistence.Entity;

import org.junit.Test;

import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.annotations.QueryProjection;
import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.expr.EString;

public class QueryProjectionTest {

    // all three annotations are needed, because all are used as entity annotations
    @QueryEntity
    @Entity
    @PersistenceCapable
    public static class EntityWithProjection{
        
        public EntityWithProjection(long id){
            
        }
        
        @QueryProjection
        public EntityWithProjection(String name){
            
        }
        
        @QueryProjection
        public EntityWithProjection(long id, CharSequence name){
            
        }
    }
    
    public static class DTOWithProjection {
        
        public DTOWithProjection(long id){
            
        }
        
        @QueryProjection
        public DTOWithProjection(String name){
            
        }
        
        @QueryProjection
        public DTOWithProjection(long id, CharSequence name){
            
        }
        
        @QueryProjection
        public DTOWithProjection(DTOWithProjection dto, long id, Long id2, String str, CharSequence c){
            
        }
    }
    
    @Test
    public void test() throws SecurityException, NoSuchMethodException{
        QEntityWithProjection.project(EString.create("")).getJavaConstructor();
        QEntityWithProjection.project(ENumber.create(0l), EString.create("")).getJavaConstructor();
        
        new QDTOWithProjection(EString.create("")).getJavaConstructor();
        new QDTOWithProjection(ENumber.create(0l), EString.create("")).getJavaConstructor();
        
    }
}
