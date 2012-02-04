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
package com.mysema.query.apt.jpa;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Version;

import com.mysema.query.annotations.QueryTransient;
import com.mysema.query.annotations.QueryType;
import com.mysema.query.apt.DefaultConfiguration;
import com.mysema.query.apt.TypeUtils;
import com.mysema.query.apt.VisitorConfig;
import com.mysema.query.codegen.Keywords;

/**
 * Configuration for {@link JPAAnnotationProcessor}
 * 
 * @author tiwe
 * @see JPAAnnotationProcessor
 */
public class JPAConfiguration extends DefaultConfiguration {

    private final List<Class<? extends Annotation>> annotations;

    public JPAConfiguration(RoundEnvironment roundEnv,Map<String,String> options,
            Class<? extends Annotation> entityAnn,
            Class<? extends Annotation> superTypeAnn,
            Class<? extends Annotation> embeddableAnn,
            Class<? extends Annotation> embeddedAnn,
            Class<? extends Annotation> skipAnn) {
        super(roundEnv, options, Keywords.JPA, null, entityAnn, superTypeAnn,
            embeddableAnn, embeddedAnn, skipAnn);
        this.annotations = getAnnotations();
    }

    @SuppressWarnings("unchecked")
    protected List<Class<? extends Annotation>> getAnnotations() {
        return Arrays.asList(Column.class, Embedded.class, EmbeddedId.class, GeneratedValue.class,
            Id.class, Version.class, JoinColumn.class, ManyToOne.class, OneToMany.class,
            PrimaryKeyJoinColumn.class, QueryType.class, QueryTransient.class);
    }

    @Override
    public VisitorConfig getConfig(TypeElement e, List<? extends Element> elements){
        boolean fields = false, methods = false;
        for (Element element : elements) {
            if (hasRelevantAnnotation(element)) {
                fields |= element.getKind().equals(ElementKind.FIELD);
                methods |= element.getKind().equals(ElementKind.METHOD);
            }
        }
        return VisitorConfig.get(fields, methods);
    }
    
    @Override
    public TypeMirror getRealType(ExecutableElement method) {
        return getManyToOneType(method);
    }

    @Override
    public TypeMirror getRealType(VariableElement field) {
        return getManyToOneType(field);
    }
    
    private TypeMirror getManyToOneType(Element element) {
        AnnotationMirror mirror = TypeUtils.getAnnotationMirrorOfType(element, ManyToOne.class);
        if (mirror != null) {
            return TypeUtils.getAnnotationValueAsTypeMirror(mirror, "targetEntity");
        } else {
            return null;
        }
    }
    
    

    private boolean hasRelevantAnnotation(Element element){
        for (Class<? extends Annotation> annotation : annotations) {
            if (element.getAnnotation(annotation) != null) {
                return true;
            }
        }
        return false;
    }

}
