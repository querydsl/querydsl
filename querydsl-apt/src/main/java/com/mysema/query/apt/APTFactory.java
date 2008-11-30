/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.apt;

import static java.util.Arrays.asList;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;

import com.mysema.query.apt.general.GeneralProcessor;
import com.sun.mirror.apt.AnnotationProcessor;
import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.apt.AnnotationProcessorFactory;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;

/**
 * APTFactory provides.
 * 
 * @author tiwe
 * @version $Id$
 */
public class APTFactory implements AnnotationProcessorFactory {
    
    static final String superClass = "javax.persistence.MappedSuperclass",
                        entity = "javax.persistence.Entity", 
                        dto = "com.mysema.query.annotations.DTO";
    
    static final Collection<String> supportedAnnotations = asList(superClass, entity, dto);

    static final Collection<String> supportedOptions = asList(
            "-AdestClass","destClass",
            "-AdestPackage","destPackage",
            "-AdtoClass","dtoClass",
            "-AdtoPackage","dtoPackage",
            "-Ainclude","include",  
            "-AnamePrefix","namePrefix");

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
            return new GeneralProcessor(env, superClass, entity, dto);
        } catch (IOException e) {
            String error = "Caught " + e.getClass().getName();
            throw new RuntimeException(error, e);
        }
    }

   
    
}