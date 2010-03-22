/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.domain;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.annotations.QueryExtensions;
import com.mysema.query.annotations.QueryMethod;
import com.mysema.query.types.constant.EStringConst;

public class QueryExtensions5Test {
    
    @QueryEntity
    public static class Entity{
        
        String prop;        
        
        @QueryMethod("{0}.method()")
        public int method(){
            return 0;
        }
        
        @QueryMethod("{0}.method({1})")
        public int method(CharSequence arg){
            return 0;
        }
        
    }
    
    @QueryEntity
    public static class Entity2{
        
        String prop;        
       
    }
    
    @QueryExtensions(Entity2.class)
    public interface Entit2yExt{
        
        @QueryMethod("{0}.method()")
        public int method();
        
        @QueryMethod("{0}.method()")
        public int method(CharSequence str);
        
        @QueryMethod("{0}.method()")
        public int method(String str);
    }
    
    @Test
    public void test_prop_and_method_available(){
        assertNotNull(QQueryExtensions5Test_Entity.entity.prop);
        assertNotNull(QQueryExtensions5Test_Entity.entity.method());
        assertNotNull(QQueryExtensions5Test_Entity.entity.method(EStringConst.create("")));
        
        assertNotNull(QQueryExtensions5Test_Entity2.entity2.prop);
        assertNotNull(QQueryExtensions5Test_Entity2.entity2.method());
        assertNotNull(QQueryExtensions5Test_Entity2.entity2.method(EStringConst.create("")));
    }

}
