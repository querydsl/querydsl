package com.mysema.query.codegen;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.mysema.codegen.model.ClassType;
import com.mysema.codegen.model.Type;

public class TypeMappingsTest {

    static class Entity{

    }

    @Test
    public void GetPathType_Of_InnerClass(){
        TypeMappings typeMappings = new TypeMappings();
        EntityType model = new EntityType(new ClassType(TypeMappingsTest.class));
        EntityType type = new EntityType(new ClassType(Entity.class));
        typeMappings.register(type, new QueryTypeFactoryImpl("Q","").create(type));

        Type pathType = typeMappings.getPathType(type, model, false);
        assertEquals("QTypeMappingsTest_Entity", pathType.getSimpleName());
    }

    @Test
    public void IsRegistered(){
        TypeMappings typeMappings = new TypeMappings();
        typeMappings.register(new ClassType(Double[].class), new ClassType(Point.class));
        assertTrue(typeMappings.isRegistered(new ClassType(Double[].class)));

    }

}
