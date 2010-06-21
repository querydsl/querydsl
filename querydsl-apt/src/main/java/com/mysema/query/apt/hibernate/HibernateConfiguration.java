/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.apt.hibernate;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.RoundEnvironment;

import com.mysema.query.apt.jpa.JPAConfiguration;

/**
 * @author tiwe
 *
 */
public class HibernateConfiguration extends JPAConfiguration{

    public HibernateConfiguration(
            RoundEnvironment roundEnv,
            Map<String,String> options,
            Class<? extends Annotation> entityAnn,
            Class<? extends Annotation> superTypeAnn,
            Class<? extends Annotation> embeddableAnn,
            Class<? extends Annotation> skipAnn) throws ClassNotFoundException {
        super(roundEnv, options, entityAnn, superTypeAnn, embeddableAnn, skipAnn);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected List<Class<? extends Annotation>> getAnnotations() throws ClassNotFoundException{
        List<Class<? extends Annotation>> annotations = super.getAnnotations();
        for (String simpleName : Arrays.asList(
                "Type",
                "Cascade",
                "LazyCollection",
                "OnDelete")){
            annotations.add((Class<? extends Annotation>) Class.forName("org.hibernate.annotations."+simpleName));
        }
        return annotations;

    }

}
