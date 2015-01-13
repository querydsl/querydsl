/*
 * Copyright 2014, Timo Westkämper
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
package com.querydsl.codegen;

import com.mysema.codegen.model.TypeCategory;
import java.lang.annotation.Annotation;

/**
 * AnnotationHelper defines a interface to provide custom annotation processing
 * for {@link TypeFactory}.
 * 
 * @author dyorgio
 */
public interface AnnotationHelper {

    /**
     * Verify if AnnotationHelper instance can handle the annotation.
     * @param annotationClass Annotation class.
     * @return {@code true} if this AnnotationHelper can handle the annotation.
     */
    boolean isSupported(Class<? extends Annotation> annotationClass);

    /**
     * Get specific object that will be used as part of type cache key.
     * @param annotation Annotation instance.
     * @return Any object, normally a annotation param. Can be {@code null}.
     */
    Object getCustomKey(Annotation annotation);

    /**
     * Get the {@link TypeCategory} according with object Class and Annotation.
     * @param cl Class of type.
     * @param annotation Annotation found on element.
     * @return Custom {@link TypeCategory}.
     */
    TypeCategory getTypeByAnnotation(Class<?> cl, Annotation annotation);
}
