package com.mysema.query.domain;

import java.util.List;

import org.junit.Test;

import com.mysema.query.annotations.QueryEntity;


public class InterfaceTypeTest {
    
    @QueryEntity
    public interface InterfaceType {
        InterfaceType getRelation();

        List<InterfaceType> getRelation2();

        List<? extends InterfaceType> getRelation3();

        int getRelation4();
        
        String getProp();
    }
    
    @QueryEntity
    public interface InterfaceType2 {

        public String getProp2();
        
    }
    
    @QueryEntity
    public interface InterfaceType3 extends InterfaceType, InterfaceType2{
        
        public String getProp3();

    }
    
    @Test
    public void test() throws SecurityException, NoSuchFieldException{
        Class<?> cl = QInterfaceType.class;
        cl.getField("relation");
        cl.getField("relation2");
        cl.getField("relation3");
        cl.getField("relation4");
    }
    
    @Test
    public void test2() throws SecurityException, NoSuchFieldException{
        Class<?> cl = QInterfaceType3.class;
        cl.getField("prop");
        cl.getField("prop2");
        cl.getField("prop3");
    }
    

}
