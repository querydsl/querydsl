/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.domain;

import java.util.Map;

import javax.jdo.annotations.PersistenceCapable;
import javax.persistence.Entity;

import org.junit.Test;

import com.mysema.query.annotations.PropertyType;
import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.annotations.QueryProjection;
import com.mysema.query.annotations.QueryType;
import com.mysema.query.types.constant.ENumberConst;
import com.mysema.query.types.constant.EStringConst;
import com.mysema.query.types.constant.ExprConst;

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
        public EntityWithProjection(@QueryType(PropertyType.SIMPLE) Long id){
            
        }
        
        @QueryProjection
        public EntityWithProjection(long id, CharSequence name){
            
        }
        
        @QueryProjection
        public EntityWithProjection(String id, CharSequence name){
            
        }
        
    }
    
    @Test
    public void entityCase(){
        QQueryProjectionTest_EntityWithProjection.create(ExprConst.create(0l)).getJavaConstructor();
        QQueryProjectionTest_EntityWithProjection.create(EStringConst.create("")).getJavaConstructor();
        QQueryProjectionTest_EntityWithProjection.create(ENumberConst.create(0l), EStringConst.create("")).getJavaConstructor();
        QQueryProjectionTest_EntityWithProjection.create(EStringConst.create(""), EStringConst.create("")).getJavaConstructor();
    }
    
    public static class DTOWithProjection {
        
        public DTOWithProjection(long id){
            
        }

        @QueryProjection
        public DTOWithProjection(@QueryType(PropertyType.SIMPLE) Long id){
            
        }
        
        @QueryProjection
        public DTOWithProjection(String name){
            
        }
                
        @QueryProjection
        public DTOWithProjection(EntityWithProjection entity){
            
        }
        
        @QueryProjection
        public DTOWithProjection(long id, CharSequence name){
            
        }
        
        @QueryProjection
        public DTOWithProjection(String id, CharSequence name){
            
        }
        
        @QueryProjection
        public DTOWithProjection(DTOWithProjection dto, long id, Long id2, String str, CharSequence c){
            
        }
        
        @QueryProjection
        public DTOWithProjection(String id, CharSequence name, Map<Long,String> map1, Map<DTOWithProjection, Long> map2){
            
        }
    }
        
    @Test
    public void dtoCase() throws SecurityException, NoSuchMethodException{   
        new QQueryProjectionTest_DTOWithProjection(ExprConst.<Long>create(0l)).getJavaConstructor();
        new QQueryProjectionTest_DTOWithProjection(EStringConst.create("")).getJavaConstructor();
        new QQueryProjectionTest_DTOWithProjection(ENumberConst.create(0l), EStringConst.create("")).getJavaConstructor();
        new QQueryProjectionTest_DTOWithProjection(EStringConst.create(""), EStringConst.create("")).getJavaConstructor();
        
    }
}
