/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.expr;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.ClassUtils;

/**
 * EConstructor represents a constructor invocation
 * 
 * @author tiwe
 * 
 * @param <D> Java type
 */
public class EConstructor<D> extends Expr<D> {
    private final List<Expr<?>> args;
    private java.lang.reflect.Constructor<D> javaConstructor;

    public EConstructor(Class<D> type, Expr<?>... args) {
        super(type);
        this.args = Collections.unmodifiableList(Arrays.asList(args));
    }

    /**
     * Get the constructor invocation arguments
     * 
     * @return
     */
    public final List<Expr<?>> getArgs() {
        return args;
    }

    /**
     * Get the constructor invocation argument with the given index
     * 
     * @param index
     * @return
     */
    public final Expr<?> getArg(int index) {
        return args.get(index);
    }

    /**
     * Returns the "real" constructor that matches the Constructor expression
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public Constructor<D> getJavaConstructor() {
        if (javaConstructor == null) {
            Class<? extends D> type = getType();
            List<Expr<?>> args = getArgs();
            for (Constructor<?> c : type.getConstructors()) {
                if (c.getParameterTypes().length == args.size()) {
                    boolean match = true;
                    for (int i = 0; i < args.size() && match; i++) {
                        Class<?> ptype = c.getParameterTypes()[i];
                        if (ptype.isPrimitive()) {
                            ptype = ClassUtils.primitiveToWrapper(ptype);
                        }
                        match &= ptype.isAssignableFrom(args.get(i).getType());
                    }
                    if (match) {
                        javaConstructor = (Constructor<D>) c;
                        return javaConstructor;
                    }
                }
            }
            throw new IllegalArgumentException("no suitable constructor found");
        } else {
            return javaConstructor;
        }
    }
}