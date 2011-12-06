/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.apt;

import java.lang.annotation.Annotation;
import java.util.Collections;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;

import com.mysema.query.annotations.QueryEmbeddable;
import com.mysema.query.annotations.QueryEmbedded;
import com.mysema.query.annotations.QueryEntities;
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
@SupportedAnnotationTypes({"com.mysema.query.annotations.*"})
public class QuerydslAnnotationProcessor extends AbstractQuerydslProcessor{
    
    @Override
    protected Configuration createConfiguration(RoundEnvironment roundEnv) {
        Class<? extends Annotation> entities = QueryEntities.class;
        Class<? extends Annotation> entity = QueryEntity.class;
        Class<? extends Annotation> superType = QuerySupertype.class;
        Class<? extends Annotation> embeddable = QueryEmbeddable.class;
        Class<? extends Annotation> embedded = QueryEmbedded.class;
        Class<? extends Annotation> skip = QueryTransient.class;
        
        return new DefaultConfiguration(
                roundEnv, processingEnv.getOptions(), Collections.<String>emptySet(), entities, entity, superType, embeddable, embedded, skip);
    }

}
