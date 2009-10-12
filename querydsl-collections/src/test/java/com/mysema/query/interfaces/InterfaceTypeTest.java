package com.mysema.query.interfaces;

import org.junit.Test;


public class InterfaceTypeTest {
    
    @Test
    public void test() throws SecurityException, NoSuchFieldException{
        Class<?> cl = QInterfaceType.class;
        cl.getField("relation");
        cl.getField("relation2");
        cl.getField("relation3");
        cl.getField("relation4");
    }

}
