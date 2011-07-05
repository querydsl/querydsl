/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.apt.hibernate;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;

import com.mysema.query.apt.DefaultConfiguration;
import com.mysema.query.apt.jpa.JPAAnnotationProcessor;

/**
 * Extended JPAAnnotationProcessor which takes Hibernate specific annotations into account
 * 
 * @author tiwe
 *
 */
@SupportedAnnotationTypes({"com.mysema.query.annotations.*","javax.persistence.*", "org.hibernate.annotations.*"})
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class HibernateAnnotationProcessor extends JPAAnnotationProcessor{

    @Override
    protected DefaultConfiguration createConfiguration(RoundEnvironment roundEnv) throws ClassNotFoundException {
        return new HibernateConfiguration(roundEnv, processingEnv.getOptions(), entity, superType, embeddable, embedded, skip);
    }

}
