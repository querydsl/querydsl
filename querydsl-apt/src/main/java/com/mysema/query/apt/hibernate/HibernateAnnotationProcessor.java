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
package com.mysema.query.apt.hibernate;

import java.lang.annotation.Annotation;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;

import com.mysema.query.apt.Configuration;
import com.mysema.query.apt.jpa.JPAAnnotationProcessor;

/**
 * Extended JPAAnnotationProcessor which takes Hibernate specific annotations into account
 * 
 * @author tiwe
 *
 */
@SupportedAnnotationTypes({"com.mysema.query.annotations.*","javax.persistence.*", "org.hibernate.annotations.*"})
public class HibernateAnnotationProcessor extends JPAAnnotationProcessor{
    
    @Override
    protected Configuration createConfiguration(RoundEnvironment roundEnv) {
        try {
            Class<? extends Annotation> entity = (Class)Class.forName("javax.persistence.Entity");
            Class<? extends Annotation> superType = (Class)Class.forName("javax.persistence.MappedSuperclass");
            Class<? extends Annotation> embeddable = (Class)Class.forName("javax.persistence.Embeddable");
            Class<? extends Annotation> embedded = (Class)Class.forName("javax.persistence.Embedded");
            Class<? extends Annotation> skip = (Class)Class.forName("javax.persistence.Transient");
            return new HibernateConfiguration(roundEnv, processingEnv.getOptions(), entity, superType, embeddable, embedded, skip);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }        
    }
    
}
