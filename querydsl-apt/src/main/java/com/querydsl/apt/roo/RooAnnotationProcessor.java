/*
 * Copyright 2013, Mysema Ltd
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
package com.querydsl.apt.roo;

import java.lang.annotation.Annotation;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;

import com.querydsl.apt.AbstractQuerydslProcessor;
import com.querydsl.apt.Configuration;
import com.querydsl.apt.DefaultConfiguration;
import com.querydsl.apt.jpa.JPAConfiguration;

/**
 * AnnotationProcessor for Spring Roo which takes {@link RooJpaEntity}, {@link RooJpaActiveRecord},
 * {@link MappedSuperclass}, {@link Embeddable} and {@link Transient} into account
 *
 * @author tiwe
 *
 */
@SupportedAnnotationTypes({"com.querydsl.core.annotations.*","javax.persistence.*","org.springframework.roo.addon.jpa.entity.*"})
public class RooAnnotationProcessor extends AbstractQuerydslProcessor {

    @Override
    protected Configuration createConfiguration(RoundEnvironment roundEnv) {
        Class<? extends Annotation> entity = RooJpaEntity.class;
        Class<? extends Annotation> superType = MappedSuperclass.class;
        Class<? extends Annotation> embeddable = Embeddable.class;
        Class<? extends Annotation> embedded = Embedded.class;
        Class<? extends Annotation> skip = Transient.class;
        DefaultConfiguration conf = new JPAConfiguration(roundEnv, processingEnv.getOptions(),
                entity, superType, embeddable, embedded, skip);
        conf.setAlternativeEntityAnnotation(RooJpaActiveRecord.class);
        return conf;
    }

}
