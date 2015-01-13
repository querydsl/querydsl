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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

/**
 * Various utility classes for {@link Element} and {@link AnnotationMirror} handling
 *
 * @author tiwe
 *
 */
public final class TypeUtils {

    public static boolean hasAnnotationOfType(Element element, Set<Class<? extends Annotation>> annotations) {
        for (Class<? extends Annotation> annotation : annotations) {
            if (hasAnnotationOfType(element, annotation)) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasAnnotationOfType(Element element, Class<? extends Annotation> annotation) {
        return element.getAnnotation(annotation) != null;
    }

    public static AnnotationMirror getAnnotationMirrorOfType(Element element, Class<? extends Annotation> annotation) {
        for (AnnotationMirror mirror : element.getAnnotationMirrors()) {
            if (mirror.getAnnotationType().toString().equals(annotation.getName())) {
                return mirror;
            }
        }
        return null;
    }

    public static boolean isAnnotationMirrorOfType(AnnotationMirror annotationMirror, Class<? extends Annotation> clazz) {
        return isAnnotationMirrorOfType(annotationMirror, clazz.getName());
    }

    public static boolean isAnnotationMirrorOfType(AnnotationMirror annotationMirror, String className) {
        String annotationClassName = annotationMirror.getAnnotationType().toString();
        return annotationClassName.equals(className);
    }

    public static Set<TypeElement> getAnnotationValuesAsElements(AnnotationMirror mirror, String method) {
        Set<TypeElement> elements = new HashSet<TypeElement>();
        for (Map.Entry<? extends ExecutableElement,? extends AnnotationValue> entry : mirror.getElementValues().entrySet()) {
            if (entry.getKey().getSimpleName().toString().equals(method)) {
                List<AnnotationValue> values = ((List) entry.getValue().getValue());
                for (AnnotationValue value : values) {
                    DeclaredType type = (DeclaredType) value.getValue();
                    elements.add((TypeElement) type.asElement());
                }
            }
        }
        return elements;
    }

    public static TypeMirror getAnnotationValueAsTypeMirror(AnnotationMirror mirror, String method) {
        for (Map.Entry<? extends ExecutableElement,? extends AnnotationValue> entry : mirror.getElementValues().entrySet()) {
            if (entry.getKey().getSimpleName().toString().equals(method)) {
                return (TypeMirror) entry.getValue().getValue();
            }
        }
        return null;
    }

    private TypeUtils() {}

}
