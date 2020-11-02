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
package com.querydsl.codegen.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author tiwe
 * 
 * @param <T>
 */
public final class MethodEvaluator<T> implements Evaluator<T> {

    private final Method method;

    private final Class<? extends T> projectionType;
    
    private final Object[] args;

    MethodEvaluator(Method method, Map<String, Object> constants, Class<? extends T> projectionType) {
        this.method = method;
        this.projectionType = projectionType;
        this.args = new Object[method.getParameterTypes().length];
        int i = args.length - constants.size();
        for (Object value : constants.values()) {
            args[i++] = value;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public T evaluate(Object... args) {
        try {
            System.arraycopy(args, 0, this.args, 0, args.length);
            return (T) method.invoke(null, this.args);
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