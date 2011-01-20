/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.codegen;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.mysema.codegen.CodeWriter;
import com.mysema.codegen.model.Parameter;
import com.mysema.codegen.model.Types;

/**
 * BeanSerializer is a Serializer implementation which serializes EntityType instances into JavaBean classes
 * 
 * @author tiwe
 *
 */
public class BeanSerializer implements Serializer{

    private final String javadocSuffix;
    
    public BeanSerializer() {
        this(" is a Querydsl bean type");
    }
    
    public BeanSerializer(String javadocSuffix) {
        this.javadocSuffix = javadocSuffix;
    }
    
    @Override
    public void serialize(EntityType model, SerializerConfig serializerConfig, CodeWriter writer) throws IOException {
        String simpleName = model.getSimpleName();
        
        // package
        if (!model.getPackageName().isEmpty()){
            writer.packageDecl(model.getPackageName());
        }
        
        // imports
        Set<String> importedClasses = getAnnotationTypes(model);
        if (model.hasLists()){
            importedClasses.add(List.class.getName());
        }
        if (model.hasMaps()){
            importedClasses.add(Map.class.getName());
        }
        writer.importClasses(importedClasses.toArray(new String[importedClasses.size()]));
        
        // javadoc        
        writer.javadoc(simpleName + javadocSuffix);
        
        // header
        for (Annotation annotation : model.getAnnotations()){
            writer.annotation(annotation);
        }               
        writer.beginClass(model);
        
        // fields
        for (Property property : model.getProperties()){
            for (Annotation annotation : property.getAnnotations()){
                writer.annotation(annotation);
            }
            writer.privateField(property.getType(), property.getEscapedName());
        }
        
        // accessors
        for (Property property : model.getProperties()){
            String propertyName = property.getEscapedName();
            // getter
            writer.beginPublicMethod(property.getType(), "get"+StringUtils.capitalize(propertyName));
            writer.line("return ", propertyName, ";");
            writer.end();
            // setter
            Parameter parameter = new Parameter(propertyName, property.getType());
            writer.beginPublicMethod(Types.VOID, "set"+StringUtils.capitalize(propertyName), parameter);
            writer.line("this.", propertyName, " = ", propertyName, ";");
            writer.end();
        }
        
        writer.end();
    }

    private Set<String> getAnnotationTypes(EntityType model) {
        Set<String> imports = new HashSet<String>();
        for (Annotation annotation : model.getAnnotations()){
            imports.add(annotation.annotationType().getName());
        }
        for (Property property : model.getProperties()){
            for (Annotation annotation : property.getAnnotations()){
                imports.add(annotation.annotationType().getName());
            }
        }
        return imports;
    }

}
