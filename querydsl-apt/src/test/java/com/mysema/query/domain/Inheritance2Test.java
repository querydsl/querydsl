/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.domain;

import org.junit.Test;

import com.mysema.query.annotations.QueryEntity;

public class Inheritance2Test {

    @QueryEntity
    public abstract class Base<T extends Base<T>>{
        Base2 base;
        Base2<?,?> base2;        
    }
        
    @QueryEntity
    public abstract class Base2<T extends Base2<T,U>,U extends IFace>{
        
    }

    @QueryEntity
    public abstract class BaseSub extends Base<BaseSub>{
        
    }
    
    @QueryEntity
    public abstract class BaseSub2<T extends BaseSub2<T>> extends Base<T>{
        
    }
    
    @QueryEntity
    public abstract class Base2Sub<T extends IFace> extends Base2<Base2Sub<T>,T>{
        
    }
    
    public interface IFace{
        
    }
    
    @Test
    public void test(){
        // TODO
    }
}
