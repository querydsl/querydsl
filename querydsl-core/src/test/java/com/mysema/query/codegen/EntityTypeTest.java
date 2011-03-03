/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.codegen;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.codegen.model.ClassType;
import com.mysema.codegen.model.TypeCategory;

public class EntityTypeTest {

//    @Test
//    public void Annotation() throws IOException{
//        Annotation annotation = new QueryExtensionsImpl(Object.class);
//        ClassType typeModel = new ClassType(TypeCategory.ENTITY, Object.class);
//        EntityType entityModel = new EntityType(typeModel);
//        entityModel.addAnnotation(annotation);
//
//        TypeMappings typeMappings = new TypeMappings();
//        EntitySerializer serializer = new EntitySerializer(typeMappings,Collections.<String>emptyList());
//        StringWriter writer = new StringWriter();
//        serializer.serialize(entityModel, SimpleSerializerConfig.DEFAULT, new JavaWriter(writer));
//        System.out.println(writer);
//    }
    
    @Test
    public void UncapSimpleName_Escaped(){
        ClassType typeModel = new ClassType(TypeCategory.ENTITY, Object.class);
        EntityType entityModel = new EntityType(typeModel);
        assertEquals("object", entityModel.getUncapSimpleName());
        
        entityModel.addProperty(new Property(entityModel, "object", typeModel));
        assertEquals("object1", entityModel.getUncapSimpleName());
    }
    
    @Test
    public void UncapSimpleName_Escaped2(){
        ClassType typeModel = new ClassType(TypeCategory.ENTITY, Object.class);
        EntityType entityModel = new EntityType(typeModel);
        assertEquals("object", entityModel.getUncapSimpleName());
        
        entityModel.addProperty(new Property(entityModel, "OBJECT", "object", typeModel, new String[0], false));
        assertEquals("object1", entityModel.getUncapSimpleName());
    }
    
}
