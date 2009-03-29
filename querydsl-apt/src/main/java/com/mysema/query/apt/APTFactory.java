/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.apt;

import static com.mysema.query.apt.APTUtils.getString;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import com.mysema.query.apt.jpa.JpaProcessor;
import com.mysema.query.apt.querydsl.QuerydslProcessor;
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
public class APTFactory implements AnnotationProcessorFactory, Constants {
    
    private static final Collection<String> supportedAnnotations = Arrays.asList(
            qdEntity, qdDto,
            jpaEntity, jpaSuperClass, jpaEmbeddable            
    );

    private static final Collection<String> supportedOptions = Collections.emptySet();

    public Collection<String> supportedAnnotationTypes() {
        return supportedAnnotations;
    }

    public Collection<String> supportedOptions() {
        return supportedOptions;
    }

    public AnnotationProcessor getProcessorFor(
            Set<AnnotationTypeDeclaration> atds,
            AnnotationProcessorEnvironment env) {
        String profile = getString(env.getOptions(), "profile", "jpa");
        if ("jpa".equals(profile)){
            return new JpaProcessor(env);
        }else if ("querydsl".equals(profile)){
            return new QuerydslProcessor(env);                       
        }else{
            throw new IllegalArgumentException("Unknown profile " + profile);
        }          
    }

}