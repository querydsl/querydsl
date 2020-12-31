/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
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

import com.querydsl.apt.DefaultConfiguration;
import com.querydsl.apt.VisitorConfig;
import com.querydsl.codegen.Keywords;
import com.querydsl.core.annotations.QueryInit;
import com.querydsl.core.annotations.QueryTransient;
import com.querydsl.core.annotations.QueryType;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.jdo.annotations.Cacheable;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.Columns;
import javax.jdo.annotations.Embedded;
import javax.jdo.annotations.Extension;
import javax.jdo.annotations.Extensions;
import javax.jdo.annotations.ForeignKey;
import javax.jdo.annotations.Index;
import javax.jdo.annotations.Join;
import javax.jdo.annotations.Key;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.Order;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Serialized;
import javax.jdo.annotations.Transactional;
import javax.jdo.annotations.Unique;
import javax.jdo.annotations.Value;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Configuration for {@link JDOAnnotationProcessor}
 *
 * @author tiwe
 * @see JDOAnnotationProcessor
 */
public class JDOConfiguration extends DefaultConfiguration {

    @SuppressWarnings("unchecked")
    private static final Iterable<Class<? extends Annotation>> relevantAnnotations
            = Collections.unmodifiableList(Arrays.asList(
                    Cacheable.class, Column.class, Columns.class,
                    javax.jdo.annotations.Element.class, Embedded.class,
                    Extension.class, Extensions.class, ForeignKey.class,
                    Index.class, Join.class, Key.class, NotPersistent.class,
                    Order.class, Persistent.class, PrimaryKey.class, QueryType.class, QueryInit.class,
                    QueryTransient.class, Serialized.class, Transactional.class, Unique.class, Value.class));

    public JDOConfiguration(ProcessingEnvironment processingEnvironment,
            RoundEnvironment roundEnv,
            Class<? extends Annotation> entitiesAnn,
            Class<? extends Annotation> entityAnn,
            Class<? extends Annotation> superTypeAnn,
            Class<? extends Annotation> embeddableAnn,
            Class<? extends Annotation> embeddedAnn, Class<? extends Annotation> skipAnn) {
        super(processingEnvironment, roundEnv, Keywords.JDO, entitiesAnn, entityAnn, superTypeAnn,
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
