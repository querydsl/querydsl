package com.mysema.query.codegen;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.codegen.model.ClassType;
import com.mysema.codegen.model.Type;

public class TypeMappingsTest {

    static class Entity{

    }

    @Test
    public void GetPathType_Of_InnerClass(){
        TypeMappings typeMappings = new TypeMappings();
        EntityType model = new EntityType("Q","", new ClassType(TypeMappingsTest.class));
        EntityType type = new EntityType("Q","", new ClassType(Entity.class));
        Type pathType = typeMappings.getPathType(type, model, false);
        assertEquals("QTypeMappingsTest_Entity", pathType.getSimpleName());
    }
}
