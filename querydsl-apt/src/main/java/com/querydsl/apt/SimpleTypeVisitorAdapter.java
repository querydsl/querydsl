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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.SimpleTypeVisitor6;

/**
 * Converts Java 8 {@link javax.lang.model.type.IntersectionType IntersectionType} instances into their first bound when visiting
 *
 * @param <R>
 * @param <P>
 */
class SimpleTypeVisitorAdapter<R, P> extends SimpleTypeVisitor6<R, P> {

    private static final Class<?> intersectionTypeClass;

    private static final Method getBoundsMethod;

    static {
        Class<?> availableClass;
        Method availableMethod;
        try {
            availableClass = Class.forName("javax.lang.model.type.IntersectionType");
            availableMethod = availableClass.getMethod("getBounds");
        } catch (Exception e) {
            // Not using Java 8
            availableClass = null;
            availableMethod = null;
        }
        intersectionTypeClass = availableClass;
        getBoundsMethod = availableMethod;
    }

    @Override
    public R visitUnknown(TypeMirror t, P p) {
        if (intersectionTypeClass != null && intersectionTypeClass.isInstance(t)) {
            try {
                List<TypeMirror> bounds = (List<TypeMirror>) getBoundsMethod.invoke(t);
                return bounds.get(0).accept(this, p);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e.getMessage(), e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        } else {
            return super.visitUnknown(t, p);
        }
    }

}
