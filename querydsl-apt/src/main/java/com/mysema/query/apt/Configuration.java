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

    TypeMappings getTypeMappings();

    VisitorConfig getConfig(TypeElement e, List<? extends Element> elements);

    Serializer getDTOSerializer();

    @Nullable
    Class<? extends Annotation> getEntitiesAnnotation();
    
    @Nullable
    Class<? extends Annotation> getEmbeddedAnnotation();
    
    @Nullable
    Class<? extends Annotation> getEmbeddableAnnotation();

    Serializer getEmbeddableSerializer();

    Class<? extends Annotation> getEntityAnnotation();

    Serializer getEntitySerializer();

    String getNamePrefix();

    SerializerConfig getSerializerConfig(EntityType model);

    @Nullable
    Class<? extends Annotation> getSkipAnnotation();

    @Nullable
    Class<? extends Annotation> getSuperTypeAnnotation();

    Serializer getSupertypeSerializer();

    boolean isBlockedField(VariableElement field);

    boolean isBlockedGetter(ExecutableElement getter);

    boolean isUseFields();

    boolean isUseGetters();

    boolean isValidConstructor(ExecutableElement constructor);

    boolean isValidField(VariableElement field);

    boolean isValidGetter(ExecutableElement getter);

    Collection<String> getKeywords();

    boolean isDefaultOverwrite();

}
