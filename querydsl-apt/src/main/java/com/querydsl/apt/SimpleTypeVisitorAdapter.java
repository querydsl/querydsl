/*
 * Copyright 2014, Mysema Ltd
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

import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.SimpleTypeVisitor6;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Converts Java 8 IntersectionType instances into their first bound when visiting
 *
 * @param <R>
 * @param <P>
 */
public class SimpleTypeVisitorAdapter<R, P> extends SimpleTypeVisitor6<R, P> {

    private static Class<?> IntersectionTypeClass;

    private static Method getBoundsMethod;

    static {
        try {
            IntersectionTypeClass = Class.forName("javax.lang.model.type.IntersectionType");
            getBoundsMethod = IntersectionTypeClass.getMethod("getBounds");
        } catch (Exception e) {}
    }

    public R visitUnknown(TypeMirror t, P p) {
        if (IntersectionTypeClass != null && IntersectionTypeClass.isInstance(t)) {
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
