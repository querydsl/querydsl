package com.mysema.query.domain;

import static org.junit.Assert.*;

import org.junit.Test;

import com.mysema.query.annotations.QueryEntity;

public class InterfaceType3Test {
    
    @QueryEntity
    interface A {
      String getA();
    }

    @QueryEntity
    interface B {
      String getB();
    }

    @QueryEntity
    interface C extends A, B {
      String getC();
    }
    
    @Test
    public void test() {
        assertNotNull(QInterfaceType3Test_C.c1.a);
        assertNotNull(QInterfaceType3Test_C.c1.b);
        assertNotNull(QInterfaceType3Test_C.c1.c);
    }

}
