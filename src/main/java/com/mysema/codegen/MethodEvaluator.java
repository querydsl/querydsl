/**
 * 
 */
package com.mysema.codegen;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.annotation.Nullable;

/**
 * @author tiwe
 *
 * @param <T>
 */
public final class MethodEvaluator<T> implements Evaluator<T> {
    
    private final Method method;
    
    @Nullable
    private final Object object;
    
    private final Class<? extends T> projectionType;

    MethodEvaluator(Method method, @Nullable Object object, Class<? extends T> projectionType) {
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