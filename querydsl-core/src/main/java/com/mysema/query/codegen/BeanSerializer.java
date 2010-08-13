/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.codegen;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.mysema.codegen.CodeWriter;

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
        Set<String> importedPackages = Collections.singleton(model.getPackageName());
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
        writer.beginClass(simpleName);
        
        // fields
        for (Property property : model.getProperties()){
            for (Annotation annotation : property.getAnnotations()){
                writer.annotation(annotation);
            }
            String propertyType = property.getType().getGenericName(true, importedPackages, importedClasses);
            writer.privateField(propertyType, property.getEscapedName());
        }
        
        // accessors
        for (Property property : model.getProperties()){
            String propertyName = property.getEscapedName();
            String propertyType = property.getType().getGenericName(true, importedPackages, importedClasses);
            // getter
            writer.beginPublicMethod(propertyType, "get"+StringUtils.capitalize(propertyName));
            writer.line("return ", propertyName, ";");
            writer.end();
            // setter
            writer.beginPublicMethod("void", "set"+StringUtils.capitalize(propertyName), propertyType + " " + propertyName);
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
