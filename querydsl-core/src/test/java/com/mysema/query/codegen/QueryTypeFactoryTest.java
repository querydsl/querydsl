package com.mysema.query.codegen;

import static org.junit.Assert.*;

import org.junit.Test;

import com.mysema.codegen.model.ClassType;
import com.mysema.codegen.model.Type;

public class QueryTypeFactoryTest {

    private Type type = new ClassType(Point.class);
    
    @Test
    public void Prefix_Only(){
        QueryTypeFactory factory = new QueryTypeFactoryImpl("Q", "", "");
        assertEquals("com.mysema.query.codegen.QPoint", factory.create(type).getFullName());
    }
    
    @Test
    public void Prefix_And_Suffix(){
        QueryTypeFactory factory = new QueryTypeFactoryImpl("Q", "Type", "");
        assertEquals("com.mysema.query.codegen.QPointType", factory.create(type).getFullName());
    }
    
    @Test
    public void Suffix_Only(){
        QueryTypeFactory factory = new QueryTypeFactoryImpl("", "Type", "");
        assertEquals("com.mysema.query.codegen.PointType", factory.create(type).getFullName());
    }
    
    @Test
    public void Prefix_And_Package_Suffix(){
        QueryTypeFactory factory = new QueryTypeFactoryImpl("Q", "", ".query");
        assertEquals("com.mysema.query.codegen.query.QPoint", factory.create(type).getFullName());
    }
    
}
