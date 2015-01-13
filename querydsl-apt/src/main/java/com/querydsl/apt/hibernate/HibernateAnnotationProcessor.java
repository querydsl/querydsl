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

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import com.querydsl.apt.Configuration;
import com.querydsl.apt.jpa.JPAAnnotationProcessor;

/**
 * HibernateAnnotationProcessor extends {@link JPAAnnotationProcessor} to take Hibernate specific 
 * annotations into account
 * 
 * @author tiwe
 * @see JPAAnnotationProcessor
 */
@SupportedAnnotationTypes({"com.querydsl.core.annotations.*","javax.persistence.*", "org.hibernate.annotations.*"})
public class HibernateAnnotationProcessor extends JPAAnnotationProcessor {
    
    @Override
    protected Configuration createConfiguration(RoundEnvironment roundEnv) {
        try {
            Class<? extends Annotation> entity = Entity.class;
            Class<? extends Annotation> superType = MappedSuperclass.class;
            Class<? extends Annotation> embeddable = Embeddable.class;
            Class<? extends Annotation> embedded = Embedded.class;
            Class<? extends Annotation> skip = Transient.class;
            return new HibernateConfiguration(roundEnv, processingEnv.getOptions(), entity, superType, 
                    embeddable, embedded, skip);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }        
    }
    
}
