/*
 * Copyright 2011, Mysema Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.codegen;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mysema.codegen.CodeWriter;

/**
 * GroovyBeanSerializer is a {@link Serializer} implementation which serializes {@link EntityType} 
 * instances into Groovy classes
 *
 * @author tiwe
 *
 */
public class GroovyBeanSerializer implements Serializer {
    
    private final boolean propertyAnnotations;
    
    private final String javadocSuffix;
    
    private boolean printSupertype = false;

    /**
     * Create a new GroovyBeanSerializer instance
     */
    public GroovyBeanSerializer() {
        this(true, " is a Querydsl bean type");
    }

    /**
     * Create a new GroovyBeanSerializer instance
     * 
     * @param javadocSuffix
     */
    public GroovyBeanSerializer(String javadocSuffix) {
        this(true, javadocSuffix);
    }
    
    /**
     * Create a new GroovyBeanSerializer instance
     * 
     * @param propertyAnnotations
     */
    public GroovyBeanSerializer(boolean propertyAnnotations) {
        this(propertyAnnotations, " is a Querydsl bean type");
    }

    /**
     * Create a new GroovyBeanSerializer instance
     * 
     * @param propertyAnnotations
     * @param javadocSuffix
     */
    public GroovyBeanSerializer(boolean propertyAnnotations, String javadocSuffix) {
        this.propertyAnnotations = propertyAnnotations;
        this.javadocSuffix = javadocSuffix;
    }

    @Override
    public void serialize(EntityType model, SerializerConfig serializerConfig, 
            CodeWriter writer) throws IOException {
        String simpleName = model.getSimpleName();

        // package
        if (!model.getPackageName().isEmpty()) {
            writer.packageDecl(model.getPackageName());
        }

        // imports
        Set<String> importedClasses = getAnnotationTypes(model);
        if (model.hasLists()) {
            importedClasses.add(List.class.getName());
        }
        if (model.hasCollections()) {
            importedClasses.add(Collection.class.getName());
        }
        if (model.hasSets()) {
            importedClasses.add(Set.class.getName());
        }
        if (model.hasMaps()) {
            importedClasses.add(Map.class.getName());
        }
        writer.importClasses(importedClasses.toArray(new String[importedClasses.size()]));

        // javadoc
        writer.javadoc(simpleName + javadocSuffix);

        // header
        for (Annotation annotation : model.getAnnotations()) {
            writer.annotation(annotation);
        }
        if (printSupertype && model.getSuperType() != null) {
            writer.beginClass(model, model.getSuperType().getType());
        } else {
            writer.beginClass(model);
        }
        
        bodyStart(model, writer);
                
        // fields
        for (Property property : model.getProperties()) {
            if (propertyAnnotations) {
                for (Annotation annotation : property.getAnnotations()) {
                    writer.annotation(annotation);
                }    
            }            
            writer.field(property.getType(), property.getEscapedName());
        }
        
        bodyEnd(model, writer);
        
        writer.end();
    }

    protected void bodyStart(EntityType model, CodeWriter writer) throws IOException {
        // template method        
    }

    protected void bodyEnd(EntityType model, CodeWriter writer) throws IOException {
        // template method        
    }

    private Set<String> getAnnotationTypes(EntityType model) {
        Set<String> imports = new HashSet<String>();
        for (Annotation annotation : model.getAnnotations()) {
            imports.add(annotation.annotationType().getName());
        }
        if (propertyAnnotations) {
            for (Property property : model.getProperties()) {
                for (Annotation annotation : property.getAnnotations()) {
                    imports.add(annotation.annotationType().getName());
                }
            }    
        }        
        return imports;
    }

    public void setPrintSupertype(boolean printSupertype) {
        this.printSupertype = printSupertype;
    }
        
}
