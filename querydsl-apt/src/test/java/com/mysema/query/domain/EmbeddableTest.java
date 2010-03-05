/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.domain;

import java.util.List;

import org.junit.Test;

import com.mysema.query.annotations.QueryEmbeddable;
import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.annotations.QuerySupertype;
import com.mysema.query.domain.AnimalTest.Cat;

public class EmbeddableTest {

    @QueryEntity
    public static class EntityWithEmbedded{
        
        public WithEntityRef e1;
        
        public WithStringProp e2;
        
        public WithEntityAndString e3;
        
        public WithList e4;
    }
    
    @QueryEmbeddable
    public static class WithEntityRef{
        
        public Cat cat;
        
    }
    
    @QueryEmbeddable
    public static class WithStringProp{
        
        public String str;
    }
    
    @QueryEmbeddable
    public static class WithEntityAndString extends WithEntityRef{
    
        public String str2;
        
    }
    
    @QueryEmbeddable
    public static class WithList extends WithStringProp{
        
        public List<Cat> cats;
        
        public String str3;
        
    }
    
    @QueryEntity
    @QueryEmbeddable
    public static class EntityAndEmbeddable{
        
    }
    
    @QuerySupertype
    @QueryEmbeddable
    public static class SuperclassAndEmbeddable{
        
    }
    
    @Test
    public void test(){
        // TODO
    }
}
