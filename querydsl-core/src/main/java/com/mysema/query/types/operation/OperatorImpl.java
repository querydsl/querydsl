/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.operation;

import static java.util.Collections.unmodifiableList;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * OperatorImpl is the default implementation of the Operator interface
 */
public class OperatorImpl<RT> implements Operator<RT> {
    private final List<Class<?>> types;

    public OperatorImpl(Class<?> type) {
        this(Collections.<Class<?>> singletonList(type));
    }

    public OperatorImpl(Class<?>... types) {
        this(Arrays.<Class<?>> asList(types));
    }

    public OperatorImpl(List<Class<?>> types) {
        this.types = unmodifiableList(types);
    }

    @Override
    public List<Class<?>> getTypes() {
        return types;
    }
}