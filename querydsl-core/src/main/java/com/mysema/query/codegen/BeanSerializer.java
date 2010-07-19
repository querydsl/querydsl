package com.mysema.query.codegen;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.HashSet;
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

    @Override
    public void serialize(EntityType model, SerializerConfig serializerConfig, CodeWriter writer) throws IOException {
        String simpleName = model.getSimpleName();
        
        // package
        if (!model.getPackageName().isEmpty()){
            writer.packageDecl(model.getPackageName());
        }
        
        // imports
        Set<Class<?>> imports = getAnnotationTypes(model);
        writer.imports(imports.toArray(new Class[imports.size()]));
        
        // javadoc        
        writer.javadoc(simpleName + " is a Querydsl bean type");
        
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
            writer.beginPublicMethod("void", "set"+StringUtils.capitalize(propertyName), propertyType + " " + propertyName);
            writer.line("this.", propertyName, " = ", propertyName, ";");
            writer.end();
        }
        
        writer.end();
    }

    private Set<Class<?>> getAnnotationTypes(EntityType model) {
        Set<Class<?>> imports = new HashSet<Class<?>>();
        for (Annotation annotation : model.getAnnotations()){
            imports.add(annotation.annotationType());
        }
        for (Property property : model.getProperties()){
            for (Annotation annotation : property.getAnnotations()){
                imports.add(annotation.annotationType());
            }
        }
        return imports;
    }

}
