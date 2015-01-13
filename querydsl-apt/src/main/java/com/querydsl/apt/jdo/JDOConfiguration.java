/*
 * Copyright 2014 Timo Westkämper
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.apt.jdo;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.RoundEnvironment;
import javax.jdo.annotations.*;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;

import com.google.common.collect.ImmutableSet;
import com.querydsl.apt.DefaultConfiguration;
import com.querydsl.apt.VisitorConfig;
import com.querydsl.codegen.Keywords;

public class JDOConfiguration extends DefaultConfiguration {

    @SuppressWarnings("unchecked")
    private static final Iterable<Class<? extends Annotation>> relevantAnnotations
            = ImmutableSet.of(
                    Cacheable.class, Column.class, Columns.class,
                    javax.jdo.annotations.Element.class, Embedded.class,
                    Extension.class, Extensions.class, ForeignKey.class,
                    Index.class, Join.class, Key.class, NotPersistent.class,
                    Order.class, Persistent.class, PrimaryKey.class,
                    Serialized.class, Transactional.class, Unique.class, Value.class);

    public JDOConfiguration(RoundEnvironment roundEnv,
            Map<String, String> options,
            Class<? extends Annotation> entitiesAnn,
            Class<? extends Annotation> entityAnn,
            Class<? extends Annotation> superTypeAnn,
            Class<? extends Annotation> embeddableAnn,
            Class<? extends Annotation> embeddedAnn, Class<? extends Annotation> skipAnn) {
        super(roundEnv, options, Keywords.JDO, entitiesAnn, entityAnn, superTypeAnn,
                embeddableAnn, embeddedAnn, skipAnn);
    }

    @Override
    public VisitorConfig getConfig(TypeElement e, List<? extends Element> elements) {
        boolean fields = false, methods = false;
        for (Element element : elements) {
            if (hasRelevantAnnotation(element)) {
                fields |= element.getKind().equals(ElementKind.FIELD);
                methods |= element.getKind().equals(ElementKind.METHOD);
            }
        }
        return VisitorConfig.get(fields, methods, VisitorConfig.FIELDS_ONLY);
    }

    private boolean hasRelevantAnnotation(Element element) {
        for (Class<? extends Annotation> relevantAnnotation : relevantAnnotations) {
            if (element.getAnnotation(relevantAnnotation) != null) {
                return true;
            }
        }
        return false;
    }

}
