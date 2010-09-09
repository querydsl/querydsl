package com.mysema.testutil;

import java.lang.annotation.Annotation;

import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import com.mysema.testutil.EmptyStatement;

public class SkipForAnnotationRule implements MethodRule {
    
    private final Class<? extends Annotation> classAnnotation;
    
    private final Class<? extends Annotation> methodAnnotation;
    
    public SkipForAnnotationRule(Class<? extends Annotation> classAnnotation, Class<? extends Annotation> methodAnnotation) {
        this.classAnnotation = classAnnotation;
        this.methodAnnotation = methodAnnotation;
    }

    @Override
    public Statement apply(Statement base, FrameworkMethod method, Object target) {
        if (target.getClass().getAnnotation(classAnnotation) != null
          && method.getMethod().getAnnotation(methodAnnotation) != null){
            return EmptyStatement.DEFAULT;
        }else{
            return base;
        }
    }

}
