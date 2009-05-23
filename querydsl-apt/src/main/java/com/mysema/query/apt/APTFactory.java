/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.apt;

import static com.mysema.query.apt.Constants.*;

import static com.mysema.query.apt.APTUtils.getString;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import com.mysema.query.apt.general.QuerydslProcessor;
import com.mysema.query.apt.jdo.JDOProcessor;
import com.mysema.query.apt.jpa.JPAProcessor;
import com.sun.mirror.apt.AnnotationProcessor;
import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.apt.AnnotationProcessorFactory;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;

/**
 * APTFactory is the main APT factory for Querydsl code generation
 * 
 * @author tiwe
 * @version $Id$
 */
public class APTFactory implements AnnotationProcessorFactory{
    
    private static final Collection<String> supportedAnnotations = Arrays.asList(
            QD_ENTITY, QD_DTO,
            JDO_ENTITY,
            JPA_ENTITY, JPA_SUPERCLASS, JPA_EMBEDDABLE            
    );

    private static final Collection<String> supportedOptions = Collections.emptySet();

    public Collection<String> supportedAnnotationTypes() {
        return supportedAnnotations;
    }

    public Collection<String> supportedOptions() {
        return supportedOptions;
    }

    public AnnotationProcessor getProcessorFor(
            Set<AnnotationTypeDeclaration> atds,
            AnnotationProcessorEnvironment env) {
        String profile = getString(env.getOptions(), "profile", "jpa");
        if ("jpa".equals(profile)){
            return new JPAProcessor(env);
        }else if ("jdo".equals(profile)){
        	return new JDOProcessor(env);
        }else if ("querydsl".equals(profile)){
            return new QuerydslProcessor(env);                       
        }else{
            throw new IllegalArgumentException("Unknown profile " + profile);
        }          
    }

}