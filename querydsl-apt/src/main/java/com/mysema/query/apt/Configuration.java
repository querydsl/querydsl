/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.apt;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nullable;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

import com.mysema.query.codegen.EntityType;
import com.mysema.query.codegen.QueryTypeFactory;
import com.mysema.query.codegen.Serializer;
import com.mysema.query.codegen.SerializerConfig;
import com.mysema.query.codegen.TypeMappings;

/**
 * Configuration defines the configuration options for APT based Querydsl code generation
 *
 * @author tiwe
 *
 */
public interface Configuration {
    
    /**
     * @return
     */
    boolean isUnknownAsEmbedded();
    
    /**
     * @return
     */
    TypeMappings getTypeMappings();

    /**
     * @param e
     * @param elements
     * @return
     */
    VisitorConfig getConfig(TypeElement e, List<? extends Element> elements);

    /**
     * @return
     */
    Serializer getDTOSerializer();

    /**
     * @return
     */
    @Nullable
    Class<? extends Annotation> getEntitiesAnnotation();

    /**
     * @return
     */
    @Nullable
    Class<? extends Annotation> getEmbeddedAnnotation();

    /**
     * @return
     */
    @Nullable
    Class<? extends Annotation> getEmbeddableAnnotation();

    /**
     * @return
     */
    Serializer getEmbeddableSerializer();

    /**
     * @return
     */
    Class<? extends Annotation> getEntityAnnotation();

    /**
     * @return
     */
    Serializer getEntitySerializer();

    /**
     * @return
     */
    String getNamePrefix();
    
    /**
     * @return
     */
    String getNameSuffix();

    /**
     * @param entityType
     * @return
     */
    SerializerConfig getSerializerConfig(EntityType entityType);

    /**
     * @return
     */
    @Nullable
    Class<? extends Annotation> getSkipAnnotation();

    /**
     * @return
     */
    @Nullable
    Class<? extends Annotation> getSuperTypeAnnotation();

    /**
     * @return
     */
    Serializer getSupertypeSerializer();

    /**
     * @param field
     * @return
     */
    boolean isBlockedField(VariableElement field);

    /**
     * @param getter
     * @return
     */
    boolean isBlockedGetter(ExecutableElement getter);

    /**
     * @return
     */
    boolean isUseFields();

    /**
     * @return
     */
    boolean isUseGetters();

    /**
     * @param constructor
     * @return
     */
    boolean isValidConstructor(ExecutableElement constructor);

    /**
     * @param field
     * @return
     */
    boolean isValidField(VariableElement field);

    /**
     * @param getter
     * @return
     */
    boolean isValidGetter(ExecutableElement getter);

    /**
     * @return
     */
    Collection<String> getKeywords();

    /**
     * @return
     */
    boolean isDefaultOverwrite();

    /**
     * @return
     */
    QueryTypeFactory getQueryTypeFactory();
    
    /**
     * @param packageName
     */
    public void addExcludedPackage(String packageName);
    
    /**
     * @param className
     */
    public void addExcludedClass(String className);
    
    /**
     * @param packageName
     * @return
     */
    boolean isExcludedPackage(String packageName);
    
    /**
     * @param className
     * @return
     */
    boolean isExcludedClass(String className);

}
