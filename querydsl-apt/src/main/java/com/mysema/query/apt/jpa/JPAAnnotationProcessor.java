/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.apt.jpa;

import java.lang.annotation.Annotation;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;

import com.mysema.query.apt.AbstractQuerydslProcessor;
import com.mysema.query.apt.Configuration;

/**
 * AnnotationProcessor for JPA which takes @Entity, @MappedSuperclass, @Embeddable and @Transient into account
 * 
 * @author tiwe
 *
 */
@SupportedAnnotationTypes({"com.mysema.query.annotations.*","javax.persistence.*"})
public class JPAAnnotationProcessor extends AbstractQuerydslProcessor {
    
    @Override
    protected Configuration createConfiguration(RoundEnvironment roundEnv) {
        try {
            Class<? extends Annotation> entity = (Class)Class.forName("javax.persistence.Entity");
            Class<? extends Annotation> superType = (Class)Class.forName("javax.persistence.MappedSuperclass");
            Class<? extends Annotation> embeddable = (Class)Class.forName("javax.persistence.Embeddable");
            Class<? extends Annotation> embedded = (Class)Class.forName("javax.persistence.Embedded");
            Class<? extends Annotation> skip = (Class)Class.forName("javax.persistence.Transient");
            return new JPAConfiguration(roundEnv, processingEnv.getOptions(), entity, superType, embeddable, embedded, skip);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }        
    }

}
