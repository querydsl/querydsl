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
import com.mysema.query.annotations.QueryExtensions;

public class EntityTypeTest {

    public static class QueryExt implements QueryExtensions{

        private final Class<?> value;

        public QueryExt(Class<?> value){
            this.value = value;
        }

        @Override
        public Class<?> value() {
            return value;
        }
        @Override
        public Class<? extends Annotation> annotationType() {
            return QueryExtensions.class;
        }
    }

    @Test
    public void Annotation() throws IOException{
        Annotation annotation = new QueryExt(Object.class);
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
