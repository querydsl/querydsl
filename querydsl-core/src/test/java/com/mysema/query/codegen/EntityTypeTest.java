/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.codegen;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.util.Collections;

import org.junit.Test;

import com.mysema.codegen.JavaWriter;
import com.mysema.codegen.model.ClassType;
import com.mysema.codegen.model.TypeCategory;

public class EntityTypeTest {

    @Test
    public void Annotation() throws IOException{
        Annotation annotation = new QueryExtensionsImpl(Object.class);
        ClassType typeModel = new ClassType(TypeCategory.ENTITY, Object.class);
        EntityType entityModel = new EntityType("Q", typeModel);
        entityModel.addAnnotation(annotation);

        TypeMappings typeMappings = new TypeMappings();
        EntitySerializer serializer = new EntitySerializer(typeMappings,Collections.<String>emptyList());
        StringWriter writer = new StringWriter();
        serializer.serialize(entityModel, SimpleSerializerConfig.DEFAULT, new JavaWriter(writer));
        System.out.println(writer);
    }
}
