/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.apt;

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

import com.mysema.query.annotations.Embeddable;
import com.mysema.query.annotations.Entity;
import com.mysema.query.annotations.Projection;
import com.mysema.query.annotations.Transient;


@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class QuerydslAnnotationProcessor extends AbstractProcessor{
    
    private Class<? extends Annotation> entity, superType, embeddable, dto, skip;
    
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Running " + getClass().getSimpleName());
        entity = Entity.class;
        superType = null;
        embeddable = Embeddable.class;
        dto = Projection.class;
        skip = Transient.class;
        
        Processor p = new Processor(processingEnv, entity, superType, embeddable, dto, skip);
        p.process(roundEnv);
        return true;
    }       
    
}
