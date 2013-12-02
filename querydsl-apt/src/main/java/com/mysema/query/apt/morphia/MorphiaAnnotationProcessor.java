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
package com.mysema.query.apt.morphia;

import java.lang.annotation.Annotation;
import java.util.Collections;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Transient;

import com.mysema.query.annotations.QueryEntities;
import com.mysema.query.annotations.QuerySupertype;
import com.mysema.query.apt.AbstractQuerydslProcessor;
import com.mysema.query.apt.Configuration;
import com.mysema.query.apt.DefaultConfiguration;

/**
 * Annotation processor to create Querydsl query types for Morphia annotated classes
 *
 * @author tiwe
 *
 */
@SupportedAnnotationTypes({"com.mysema.query.annotations.*","org.mongodb.morphia.annotations.*"})
public class MorphiaAnnotationProcessor extends AbstractQuerydslProcessor {

    @Override
    protected Configuration createConfiguration(RoundEnvironment roundEnv) {
        Class<? extends Annotation> entities = QueryEntities.class;
        Class<? extends Annotation> entity = Entity.class;
        Class<? extends Annotation> superType = QuerySupertype.class;
        Class<? extends Annotation> embedded = Embedded.class;
        Class<? extends Annotation> skip = Transient.class;
        DefaultConfiguration conf = new DefaultConfiguration(roundEnv,
                processingEnv.getOptions(), Collections.<String>emptySet(),
                entities, entity, superType, null, embedded, skip);
        try {
            Class cl = Class.forName("com.mysema.query.mongodb.Point");
            conf.addCustomType(Double[].class, cl);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
        return conf;
    }

}
