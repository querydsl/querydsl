/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.mongodb.morphia;

import java.lang.annotation.Annotation;
import java.util.Collections;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Transient;
import com.mysema.query.annotations.QueryEntities;
import com.mysema.query.apt.AbstractQuerydslProcessor;
import com.mysema.query.apt.Configuration;
import com.mysema.query.apt.DefaultConfiguration;

/**
 * Annotation processor to create Querydsl query types for Morphia annoated classes
 *
 * @author tiwe
 *
 */
@SupportedAnnotationTypes({"com.mysema.query.annotations.*","com.google.code.morphia.annotations.*"})
public class MorphiaAnnotationProcessor extends AbstractQuerydslProcessor {
    
    @Override
    protected Configuration createConfiguration(RoundEnvironment roundEnv) {
        Class<? extends Annotation> entities = QueryEntities.class;
        Class<? extends Annotation> entity = Entity.class;
        Class<? extends Annotation> embedded = Embedded.class;
        Class<? extends Annotation> skip = Transient.class;
        return new DefaultConfiguration(roundEnv, processingEnv.getOptions(), Collections.<String>emptySet(), 
                entities, entity, null, null, embedded, skip);
    }

}
