/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.apt.jdo;

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

import com.mysema.query.apt.Configuration;
import com.mysema.query.apt.Processor;

/**
 * @author tiwe
 *
 */
@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class JDOAnnotationProcessor extends AbstractProcessor{
    
    private Class<? extends Annotation> entity, superType, embeddable, skip;
    
    @SuppressWarnings("unchecked")
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        try {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Running " + getClass().getSimpleName());
            entity = (Class)Class.forName("javax.jdo.annotations.PersistenceCapable");
            superType = null; // ?!?
            embeddable = (Class)Class.forName("javax.jdo.annotations.EmbeddedOnly");
            skip = (Class)Class.forName("javax.jdo.annotations.NotPersistent");
            
            Configuration configuration = new Configuration(entity, superType, embeddable, skip);
            configuration.setUseGetters(false);
            Processor processor = new Processor(processingEnv, configuration);
            processor.process(roundEnv);
            return true;
            
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }       
    
}