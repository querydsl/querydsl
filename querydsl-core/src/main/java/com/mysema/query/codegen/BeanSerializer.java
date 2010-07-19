package com.mysema.query.codegen;

import java.io.IOException;
import java.lang.annotation.Annotation;

import org.apache.commons.lang.StringUtils;

import com.mysema.codegen.CodeWriter;

public class BeanSerializer implements Serializer{

    @Override
    public void serialize(EntityType model, SerializerConfig serializerConfig, CodeWriter writer) throws IOException {
        String simpleName = model.getSimpleName();
        
        // package
        if (!model.getPackageName().isEmpty()){
            writer.packageDecl(model.getPackageName());
        }
        
        // javadoc        
        writer.javadoc(simpleName + " is a Querydsl bean type for " + simpleName);
        
        // header
        for (Annotation annotation : model.getAnnotations()){
            writer.annotation(annotation);
        }               
        writer.beginClass(simpleName);
        
        // fields
        for (Property property : model.getProperties()){
            String propertyType = property.getType().getLocalGenericName(model, false);
            writer.privateField(propertyType, property.getEscapedName());
        }
        
        // accessors
        for (Property property : model.getProperties()){
            String propertyName = property.getEscapedName();
            // TODO : serialize property annotations
            String propertyType = property.getType().getLocalGenericName(model, false);
            // getter
            writer.beginPublicMethod(propertyType, "get"+StringUtils.capitalize(propertyName));
            writer.line("return ", propertyName, ";");
            writer.end();
            // setter
            writer.beginPublicMethod("void", "set"+StringUtils.capitalize(propertyName), propertyType, propertyName);
            writer.line("this.", propertyName, " = ", propertyName, ";");
            writer.end();
        }
        
        writer.end();
    }

}
