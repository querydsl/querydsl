package com.mysema.query.domain;

import javax.jdo.annotations.PersistenceCapable;
import javax.persistence.Entity;

import org.junit.Test;

import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.annotations.QueryProjection;
import com.mysema.query.types.expr.ENumberConst;
import com.mysema.query.types.expr.EStringConst;

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
        QEntityWithProjection.project(EStringConst.create("")).getJavaConstructor();
        QEntityWithProjection.project(ENumberConst.create(0l), EStringConst.create("")).getJavaConstructor();
        
        new QDTOWithProjection(EStringConst.create("")).getJavaConstructor();
        new QDTOWithProjection(ENumberConst.create(0l), EStringConst.create("")).getJavaConstructor();
        
    }
}
