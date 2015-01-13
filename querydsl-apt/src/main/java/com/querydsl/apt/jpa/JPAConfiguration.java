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

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import javax.persistence.*;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.querydsl.core.annotations.PropertyType;
import com.querydsl.core.annotations.QueryEntities;
import com.querydsl.core.annotations.QueryTransient;
import com.querydsl.core.annotations.QueryType;
import com.querydsl.apt.*;
import com.querydsl.codegen.Keywords;
import com.querydsl.core.util.Annotations;

/**
 * Configuration for {@link JPAAnnotationProcessor}
 *
 * @author tiwe
 * @see JPAAnnotationProcessor
 */
public class JPAConfiguration extends DefaultConfiguration {

    private final List<Class<? extends Annotation>> annotations;

    private final Types types;

    public JPAConfiguration(RoundEnvironment roundEnv,
            Map<String,String> options,
            Class<? extends Annotation> entityAnn,
            Class<? extends Annotation> superTypeAnn,
            Class<? extends Annotation> embeddableAnn,
            Class<? extends Annotation> embeddedAnn,
            Class<? extends Annotation> skipAnn) {
        super(roundEnv, options, Keywords.JPA, QueryEntities.class, entityAnn, superTypeAnn,
            embeddableAnn, embeddedAnn, skipAnn);
        this.annotations = getAnnotations();
        // TODO replace with proper injection in Querydsl 4.0.0
        this.types = AbstractQuerydslProcessor.TYPES;
        setStrictMode(true);
    }

    @SuppressWarnings("unchecked")
    protected List<Class<? extends Annotation>> getAnnotations() {
        return ImmutableList.of(
            Access.class, Basic.class, Column.class, ElementCollection.class,
            Embedded.class, EmbeddedId.class, Enumerated.class, GeneratedValue.class, Id.class,
            JoinColumn.class, ManyToOne.class, ManyToMany.class, MapKeyEnumerated.class,
            OneToOne.class, OneToMany.class, PrimaryKeyJoinColumn.class, QueryType.class,
            QueryTransient.class, Temporal.class, Transient.class, Version.class);
    }

    @Override
    public VisitorConfig getConfig(TypeElement e, List<? extends Element> elements) {
        Access access = e.getAnnotation(Access.class);
        if (access != null) {
            if (access.value() == AccessType.FIELD) {
                return VisitorConfig.FIELDS_ONLY;
            } else {
                return VisitorConfig.METHODS_ONLY;
            }
        }
        boolean fields = false, methods = false;
        for (Element element : elements) {
            if (hasRelevantAnnotation(element)) {
                fields |= element.getKind().equals(ElementKind.FIELD);
                methods |= element.getKind().equals(ElementKind.METHOD);
            }
        }
        return VisitorConfig.get(fields, methods, VisitorConfig.ALL);
    }

    @Override
    public TypeMirror getRealType(ExecutableElement method) {
        return getRealElementType(method);
    }

    @Override
    public TypeMirror getRealType(VariableElement field) {
        return getRealElementType(field);
    }

    private TypeMirror getRealElementType(Element element) {
        AnnotationMirror mirror = TypeUtils.getAnnotationMirrorOfType(element, ManyToOne.class);
        if (mirror == null) {
            mirror = TypeUtils.getAnnotationMirrorOfType(element, OneToOne.class);
        }
        if (mirror != null) {
            return TypeUtils.getAnnotationValueAsTypeMirror(mirror, "targetEntity");
        }

        mirror = TypeUtils.getAnnotationMirrorOfType(element, OneToMany.class);
        if (mirror == null) {
            mirror = TypeUtils.getAnnotationMirrorOfType(element, ManyToMany.class);
        }
        if (mirror != null) {
            TypeMirror typeArg = TypeUtils.getAnnotationValueAsTypeMirror(mirror, "targetEntity");
            TypeMirror erasure = types.erasure(element.asType());
            TypeElement typeElement = (TypeElement) types.asElement(erasure);
            if (typeElement != null && typeArg != null) {
                if (typeElement.getTypeParameters().size() == 1) {
                    return types.getDeclaredType(typeElement, typeArg);
                } else if (typeElement.getTypeParameters().size() == 2) {
                    if (element.asType() instanceof DeclaredType) {
                        TypeMirror first = ((DeclaredType)element.asType()).getTypeArguments().get(0);
                        return types.getDeclaredType(typeElement, first, typeArg);
                    }
                }
            }
        }

        return null;
    }

    @Override
    public void inspect(Element element, Annotations annotations) {
        Temporal temporal = element.getAnnotation(Temporal.class);
        if (temporal != null && element.getAnnotation(ElementCollection.class) == null) {
            PropertyType propertyType = null;
            switch (temporal.value()) {
            case DATE: propertyType = PropertyType.DATE; break;
            case TIME: propertyType = PropertyType.TIME; break;
            case TIMESTAMP: propertyType = PropertyType.DATETIME;
            }
            annotations.addAnnotation(new QueryTypeImpl(propertyType));
        }
    }

    private boolean hasRelevantAnnotation(Element element) {
        for (Class<? extends Annotation> annotation : annotations) {
            if (element.getAnnotation(annotation) != null) {
                return true;
            }
        }
        return false;
    }

}
