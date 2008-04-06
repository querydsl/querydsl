/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.apt;

import static java.util.Collections.unmodifiableList;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

import com.mysema.query.apt.hibernate.HibernateProcessor;
import com.sun.mirror.apt.AnnotationProcessor;
import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.apt.AnnotationProcessorFactory;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;

/**
 * 
 * APTFactory provides
 * 
 * @author tiwe
 * @version $Id$ 
 * 
 */
public class APTFactory implements AnnotationProcessorFactory {
    
    private static final Collection<String> supportedAnnotations = unmodifiableList(Arrays
            .asList("*"));

    private static final Collection<String> supportedOptions = 
        Arrays.asList("-AdestClass","-AdtoClass","-Ainclude", "-AnamePrefix");

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
            // TODO : creator processor based on profile
            return new HibernateProcessor(env);
        } catch (IOException e) {
            String error = "Caught " + e.getClass().getName();
            throw new RuntimeException(error, e);
        }
    }

   
    
}