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
