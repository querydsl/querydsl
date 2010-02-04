package com.mysema.query.codegen;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.annotation.Annotation;

import org.junit.Test;

import com.mysema.query.annotations.QueryExtensions;

public class EntityModelTest {

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
    public void annotation() throws IOException{
        Annotation annotation = new QueryExt(Object.class);
        ClassTypeModel typeModel = new ClassTypeModel(TypeCategory.ENTITY, Object.class);
        EntityModel entityModel = new EntityModel("Q", typeModel);
        entityModel.addAnnotation(annotation);
        
        TypeMappings typeMappings = new TypeMappings();
        EntitySerializer serializer = new EntitySerializer(typeMappings);
        StringWriter writer = new StringWriter();
        serializer.serialize(entityModel, SimpleSerializerConfig.DEFAULT, writer);
        System.out.println(writer);
    }
}
