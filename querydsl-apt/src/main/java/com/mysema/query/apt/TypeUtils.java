package com.mysema.query.apt;

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;

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
    
    public static boolean isAnnotationMirrorOfType(AnnotationMirror annotationMirror, Class<? extends Annotation> clazz) {
        return isAnnotationMirrorOfType(annotationMirror, clazz.getName());
    }
    
    public static boolean isAnnotationMirrorOfType(AnnotationMirror annotationMirror, String className) {
        String annotationClassName = annotationMirror.getAnnotationType().toString();
        return annotationClassName.equals( className );
    }
    
    private TypeUtils() {}
    
}
