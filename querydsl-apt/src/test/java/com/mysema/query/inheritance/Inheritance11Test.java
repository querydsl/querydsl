package com.mysema.query.inheritance;

import static org.junit.Assert.*;

import org.junit.Test;

import com.mysema.query.annotations.QueryEntity;

public class Inheritance11Test {
    
    @QueryEntity
    public class Foo extends FooBase<Foo> {
     
    }

    @QueryEntity
    public class FooBase<T> {
        
    }

    @QueryEntity
    public class BarBase<T> {
        
    }
    
    @QueryEntity
    public class Bar extends BarBase<Foo> {
     
    }
    
    @Test
    public void test(){
        assertNotNull(QInheritance11Test_Foo.foo);
        assertNotNull(QInheritance11Test_FooBase.fooBase);
        assertNotNull(QInheritance11Test_Bar.bar);
        assertNotNull(QInheritance11Test_BarBase.barBase);
    }

}
