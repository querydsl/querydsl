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
package com.querydsl.maven;

import com.mysema.codegen.model.TypeCategory;
import com.querydsl.codegen.AnnotationHelper;
import java.lang.annotation.Annotation;
import javax.persistence.Temporal;

/**
 * A {@link AnnotationHelper} that handles JPA {@link Temporal} annotation.
 * 
 * @author dyorgio
 */
public class JPATemporalAnnotationHelper implements AnnotationHelper {
        
        public static final JPATemporalAnnotationHelper INSTANCE = new JPATemporalAnnotationHelper();

        private JPATemporalAnnotationHelper() {
        }

        @Override
        public boolean isSupported(Class<? extends Annotation> annotationClass) {
            return Temporal.class.isAssignableFrom(annotationClass);
        }

        @Override
        public Object getCustomKey(Annotation annotation) {
            return ((Temporal) annotation).value();
        }

        @Override
        public TypeCategory getTypeByAnnotation(Class<?> cl, Annotation annotation) {
            switch (((Temporal) annotation).value()) {
                case DATE:
                    return TypeCategory.DATE;
                case TIME:
                    return TypeCategory.TIME;
                case TIMESTAMP:
                    return TypeCategory.DATETIME;
            }
            return null;
        }
    }
