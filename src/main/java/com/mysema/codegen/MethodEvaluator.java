/*
 * Copyright 2010, Mysema Ltd
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
package com.mysema.codegen;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author tiwe
 * 
 * @param <T>
 */
public final class MethodEvaluator<T> implements Evaluator<T> {

    private final Method method;

    private final Object object;

    private final Class<? extends T> projectionType;

    MethodEvaluator(Method method, Object object, Class<? extends T> projectionType) {
        this.method = method;
        this.object = object;
        this.projectionType = projectionType;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T evaluate(Object... args) {
        try {
            return (T) method.invoke(object, args);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        } catch (InvocationTargetException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public Class<? extends T> getType() {
        return projectionType;
    }
}