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

import com.mysema.query.annotations.QueryEmbeddable;
import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.annotations.QuerySupertype;
import com.mysema.query.annotations.QueryTransient;

/**
 * Default annotation processor for Querydsl which handles @QueryEntity, @QuerySupertype, 
 * @QueryEmbeddable and @QueryTransient
 * 
 * @author tiwe
 *
 */
@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class QuerydslAnnotationProcessor extends AbstractProcessor{

    private Class<? extends Annotation> entity, superType, embeddable, skip;

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Running " + getClass().getSimpleName());
        entity = QueryEntity.class;
        superType = QuerySupertype.class;
        embeddable = QueryEmbeddable.class;
        skip = QueryTransient.class;

        DefaultConfiguration configuration = new DefaultConfiguration(roundEnv, processingEnv.getOptions(), entity, superType, embeddable, skip);

        Processor processor = new Processor(processingEnv, roundEnv, configuration);
        processor.process();
        return true;
    }

}
