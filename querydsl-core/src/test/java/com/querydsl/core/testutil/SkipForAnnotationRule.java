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
package com.querydsl.core.testutil;

import java.lang.annotation.Annotation;

import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

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
          && method.getMethod().getAnnotation(methodAnnotation) != null) {
            return EmptyStatement.DEFAULT;
        } else {
            return base;
        }
    }

}
