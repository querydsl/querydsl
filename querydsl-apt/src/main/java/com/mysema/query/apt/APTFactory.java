/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.apt;

import static com.mysema.query.apt.Constants.JDO_ENTITY;
import static com.mysema.query.apt.Constants.JPA_EMBEDDABLE;
import static com.mysema.query.apt.Constants.JPA_ENTITY;
import static com.mysema.query.apt.Constants.JPA_SUPERCLASS;
import static com.mysema.query.apt.Constants.QD_DTO;
import static com.mysema.query.apt.Constants.QD_ENTITY;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import com.mysema.query.apt.general.QuerydslProcessor;
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

    private static final Collection<String> supportedAnnotations = Arrays
            .asList(QD_ENTITY, QD_DTO, JDO_ENTITY, JPA_ENTITY, JPA_SUPERCLASS,
                    JPA_EMBEDDABLE);

    private static final Collection<String> supportedOptions = Collections
            .emptySet();

    public Collection<String> supportedAnnotationTypes() {
        return supportedAnnotations;
    }

    public Collection<String> supportedOptions() {
        return supportedOptions;
    }

    public AnnotationProcessor getProcessorFor(
            Set<AnnotationTypeDeclaration> atds,
            AnnotationProcessorEnvironment env) {
        return new QuerydslProcessor(env);
    }

}