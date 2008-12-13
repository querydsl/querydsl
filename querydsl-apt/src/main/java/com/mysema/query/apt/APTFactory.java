/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.apt;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import com.mysema.query.apt.general.GeneralProcessor;
import com.sun.mirror.apt.AnnotationProcessor;
import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.apt.AnnotationProcessorFactory;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;

/**
 * APTFactory is the main APT factory for Querydsl code generation
 * 
 * @author tiwe
 * @version $Id$
 */
public class APTFactory implements AnnotationProcessorFactory {

    static final String jpaSuperClass = "javax.persistence.MappedSuperclass",
                        jpaEntity = "javax.persistence.Entity",                        
                        jpaEmbeddable = "javax.persistence.Embeddable",
                        qdEntity= "com.mysema.query.annotations.Domain",
                        qdDto = "com.mysema.query.annotations.DTO";

    static final Collection<String> supportedAnnotations = 
        Arrays.asList(jpaSuperClass, jpaEntity, jpaEmbeddable, qdEntity, qdDto);

    static final Collection<String> supportedOptions = Collections.emptySet();

    public Collection<String> supportedAnnotationTypes() {
        return supportedAnnotations;
    }

    public Collection<String> supportedOptions() {
        return supportedOptions;
    }

    public AnnotationProcessor getProcessorFor(
            Set<AnnotationTypeDeclaration> atds,
            AnnotationProcessorEnvironment env) {
        try {
            return new GeneralProcessor(env, jpaSuperClass, jpaEntity, qdDto, jpaEmbeddable);
        } catch (IOException e) {
            String error = "Caught " + e.getClass().getName();
            throw new RuntimeException(error, e);
        }
    }

}