package com.mysema.query.domain;

import org.junit.Test;

import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.annotations.QueryExclude;

@QueryExclude
@QueryEntity
public class ExcludedClassTest {

    @QueryExclude
    @QueryEntity
    public class InnerClass {
        
    }
    
    @Test(expected=ClassNotFoundException.class)
    public void OuterClass() throws ClassNotFoundException {
        Class.forName(getClass().getPackage().getName() + ".Q" + getClass().getSimpleName()); 
    }
    
    @Test(expected=ClassNotFoundException.class)
    public void InnerClass() throws ClassNotFoundException {
        Class.forName(getClass().getPackage().getName() + ".QExcludedClassTest_InnerClass");
    }
    
}
