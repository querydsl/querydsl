/*
 * Copyright (c) 2011 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.apt;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

/**
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
    
    public static Set<Element> getAnnotationValuesAsElements(AnnotationMirror mirror, String method) {
        Set<Element> elements = new HashSet<Element>();
        for (Map.Entry<? extends ExecutableElement,? extends AnnotationValue> entry : mirror.getElementValues().entrySet()) {
            if (entry.getKey().getSimpleName().toString().equals("value")) {
                List<AnnotationValue> values = (List) entry.getValue().getValue();
                for (AnnotationValue value : values) {
                    DeclaredType type = (DeclaredType) value.getValue();
                    elements.add(type.asElement());
                }
            }
        }
        return elements;
    }
    
    public static TypeMirror getAnnotationValueAsTypeMirror(AnnotationMirror mirror, String method) {
        for (Map.Entry<? extends ExecutableElement,? extends AnnotationValue> entry : mirror.getElementValues().entrySet()) {
            if (entry.getKey().getSimpleName().toString().equals("value")) {
                return (TypeMirror) entry.getValue().getValue();                
            }
        }
        return null;
    }
    
    private TypeUtils() {}
    
}
