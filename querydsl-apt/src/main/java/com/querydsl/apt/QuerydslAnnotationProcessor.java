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
package com.querydsl.apt;

import java.lang.annotation.Annotation;
import java.util.Collections;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;

import com.querydsl.core.annotations.QueryEmbeddable;
import com.querydsl.core.annotations.QueryEmbedded;
import com.querydsl.core.annotations.QueryEntities;
import com.querydsl.core.annotations.QueryEntity;
import com.querydsl.core.annotations.QuerySupertype;
import com.querydsl.core.annotations.QueryTransient;

/**
 * Default annotation processor for Querydsl which handles {@link QueryEntity}, {@link QuerySupertype}, 
 * {@link QueryEmbeddable}, {@link QueryEmbedded} and {@link QueryTransient}
 * 
 * @author tiwe
 *
 */
@SupportedAnnotationTypes({"com.querydsl.core.annotations.*"})
public class QuerydslAnnotationProcessor extends AbstractQuerydslProcessor {
    
    @Override
    protected Configuration createConfiguration(RoundEnvironment roundEnv) {
        Class<? extends Annotation> entities = QueryEntities.class;
        Class<? extends Annotation> entity = QueryEntity.class;
        Class<? extends Annotation> superType = QuerySupertype.class;
        Class<? extends Annotation> embeddable = QueryEmbeddable.class;
        Class<? extends Annotation> embedded = QueryEmbedded.class;
        Class<? extends Annotation> skip = QueryTransient.class;
        
        return new DefaultConfiguration(
                roundEnv, processingEnv.getOptions(), Collections.<String>emptySet(), entities, 
                entity, superType, embeddable, embedded, skip);
    }

}
