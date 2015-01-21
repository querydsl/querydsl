/*
 * Copyright 2011, Mysema Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.apt.hibernate;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.RoundEnvironment;

import com.querydsl.apt.jpa.JPAConfiguration;

/**
 * Configuration for {@link HibernateAnnotationProcessor}
 * 
 * @author tiwe
 * @see HibernateAnnotationProcessor
 * @see JPAConfiguration
 */
public class HibernateConfiguration extends JPAConfiguration {

    public HibernateConfiguration(
            RoundEnvironment roundEnv,
            Map<String,String> options,
            Class<? extends Annotation> entityAnn,
            Class<? extends Annotation> superTypeAnn,
            Class<? extends Annotation> embeddableAnn,
            Class<? extends Annotation> embeddedAnn,
            Class<? extends Annotation> skipAnn) throws ClassNotFoundException {
        super(roundEnv, options, entityAnn, superTypeAnn, embeddableAnn, embeddedAnn, skipAnn);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected List<Class<? extends Annotation>> getAnnotations() {
        try {            
            List<Class<? extends Annotation>> annotations = new ArrayList<Class<? extends Annotation>>();
            annotations.addAll(super.getAnnotations());
            for (String simpleName : Arrays.asList("Type", "Cascade", "LazyCollection", "OnDelete")) {
                annotations.add((Class<? extends Annotation>) Class.forName("org.hibernate.annotations."+simpleName));
            }
            return annotations;    
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    
}
