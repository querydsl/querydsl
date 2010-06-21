/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.HashMap;
import java.util.Map;

/**
 * @author tiwe
 *
 */
public class AnnotatedElementAdapter implements AnnotatedElement{

    private final Map<Class<?>,Annotation> annotations = new HashMap<Class<?>,Annotation>();

    public AnnotatedElementAdapter(AnnotatedElement... elements){
        for (AnnotatedElement element : elements){
            for (Annotation annotation : element.getAnnotations()){
                annotations.put(annotation.annotationType(), annotation);
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        return (T) annotations.get(annotationClass);
    }

    @Override
    public Annotation[] getAnnotations() {
        return annotations.values().toArray(new Annotation[annotations.values().size()]);
    }

    @Override
    public Annotation[] getDeclaredAnnotations() {
        return getAnnotations();
    }

    @Override
    public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
        return annotations.containsKey(annotationClass);
    }

}
