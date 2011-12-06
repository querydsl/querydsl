/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.apt.jdo;

import java.lang.annotation.Annotation;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;

import com.mysema.query.apt.AbstractQuerydslProcessor;
import com.mysema.query.apt.Configuration;
import com.mysema.query.apt.DefaultConfiguration;

/**
 * AnnotationProcessor for JDO which takes @PersistenceCapable, @EmbeddedOnly and @NotPersistent into account
 * 
 * @author tiwe
 *
 */
@SupportedAnnotationTypes({"com.mysema.query.annotations.*","javax.jdo.annotations.*"})
public class JDOAnnotationProcessor extends AbstractQuerydslProcessor {

    @Override
    protected Configuration createConfiguration(RoundEnvironment roundEnv) {
        try {
            Class<? extends Annotation> entity = (Class)Class.forName("javax.jdo.annotations.PersistenceCapable");
            Class<? extends Annotation> embeddable = (Class)Class.forName("javax.jdo.annotations.EmbeddedOnly");
            Class<? extends Annotation> embedded = (Class)Class.forName("javax.jdo.annotations.Embedded");
            Class<? extends Annotation> skip = (Class)Class.forName("javax.jdo.annotations.NotPersistent");
            return new DefaultConfiguration(roundEnv, processingEnv.getOptions(), Keywords.keywords, null, entity, null, embeddable, embedded, skip);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }        
    }
}
