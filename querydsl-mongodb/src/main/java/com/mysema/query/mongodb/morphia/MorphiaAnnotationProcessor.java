/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.mongodb.morphia;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Transient;
import com.mysema.query.annotations.QueryEntities;
import com.mysema.query.apt.DefaultConfiguration;
import com.mysema.query.apt.Processor;
import com.mysema.query.mongodb.Point;

/**
 * Annotation processor to create Querydsl query types for Morphia annoated classes
 *
 * @author tiwe
 *
 */
@SupportedAnnotationTypes({"com.mysema.query.annotations.*","com.google.code.morphia.annotations.*"})
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class MorphiaAnnotationProcessor extends AbstractProcessor{

    private static final Boolean ALLOW_OTHER_PROCESSORS_TO_CLAIM_ANNOTATIONS = Boolean.FALSE;
    
    private Class<? extends Annotation> entities, entity, embedded, skip;

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Running " + getClass().getSimpleName());
        entities = QueryEntities.class;
        entity = Entity.class;
        embedded = Embedded.class;
        skip = Transient.class;

        DefaultConfiguration configuration = new DefaultConfiguration(
                roundEnv, processingEnv.getOptions(), Collections.<String>emptySet(),
                entities, entity, null, null, embedded, skip);
        configuration.addCustomType(Double[].class, Point.class);

        Processor processor = new Processor(processingEnv, roundEnv, configuration);
        processor.process();
        return ALLOW_OTHER_PROCESSORS_TO_CLAIM_ANNOTATIONS;
    }

}
