/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
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

import com.querydsl.codegen.EntityType;
import com.querydsl.codegen.QueryTypeFactory;
import com.querydsl.codegen.Serializer;
import com.querydsl.codegen.SerializerConfig;
import com.querydsl.codegen.TypeMappings;
import com.querydsl.core.util.Annotations;

import org.jetbrains.annotations.Nullable;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

/**
 * {@code Configuration} defines the configuration options for APT-based Querydsl code generation
 *
 * @author tiwe
 *
 */
public interface Configuration {

    boolean isUnknownAsEmbedded();

    TypeMappings getTypeMappings();

    VisitorConfig getConfig(TypeElement e, List<? extends Element> elements);

    Serializer getDTOSerializer();

    @Nullable
    Class<? extends Annotation> getEntitiesAnnotation();

    @Nullable
    Class<? extends Annotation> getEmbeddedAnnotation();

    @Nullable
    Class<? extends Annotation> getEmbeddableAnnotation();

    Serializer getEmbeddableSerializer();

    Class<? extends Annotation> getEntityAnnotation();

    Class<? extends Annotation> getAlternativeEntityAnnotation();

    Set<Class<? extends Annotation>> getEntityAnnotations();

    Serializer getEntitySerializer();

    String getNamePrefix();

    String getNameSuffix();

    SerializerConfig getSerializerConfig(EntityType entityType);

    @Nullable
    Class<? extends Annotation> getSkipAnnotation();

    @Nullable
    Class<? extends Annotation> getSuperTypeAnnotation();

    Serializer getSupertypeSerializer();

    boolean isBlockedField(VariableElement field);

    boolean isBlockedGetter(ExecutableElement getter);

    boolean isUseFields();

    boolean isUseGetters();

    boolean isValidConstructor(ExecutableElement constructor);

    boolean isValidField(VariableElement field);

    boolean isValidGetter(ExecutableElement getter);

    Collection<String> getKeywords();

    QueryTypeFactory getQueryTypeFactory();

    void addExcludedPackage(String packageName);

    void addExcludedClass(String className);

    TypeMirror getRealType(ExecutableElement method);

    TypeMirror getRealType(VariableElement field);

    boolean isExcludedPackage(String packageName);

    boolean isExcludedClass(String className);

    void inspect(Element element, Annotations annotations);

    boolean isStrictMode();

    Function<EntityType, String> getVariableNameFunction();

}
