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
package com.querydsl.apt.jpa;

import java.lang.annotation.Annotation;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import com.querydsl.apt.AbstractQuerydslProcessor;
import com.querydsl.apt.Configuration;

/**
 * AnnotationProcessor for JPA which takes {@link Entity}, {@link MappedSuperclass}, {@link Embeddable}
 * and {@link Transient} into account
 *
 * @author tiwe
 *
 */
@SupportedAnnotationTypes({"com.querydsl.core.annotations.*","javax.persistence.*"})
public class JPAAnnotationProcessor extends AbstractQuerydslProcessor {

    @Override
    protected Configuration createConfiguration(RoundEnvironment roundEnv) {
        Class<? extends Annotation> entity = Entity.class;
        Class<? extends Annotation> superType = MappedSuperclass.class;
        Class<? extends Annotation> embeddable = Embeddable.class;
        Class<? extends Annotation> embedded = Embedded.class;
        Class<? extends Annotation> skip = Transient.class;
        return new JPAConfiguration(roundEnv, processingEnv.getOptions(),
                entity, superType, embeddable, embedded, skip);
    }

}
