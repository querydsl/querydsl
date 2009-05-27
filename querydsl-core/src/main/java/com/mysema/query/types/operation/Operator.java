package com.mysema.query.types.operation;

import static java.util.Collections.unmodifiableList;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * The Class Op represents an Operator symbol.
 */
public class Operator<RT> {
    private final List<Class<?>> types;

    public Operator(Class<?> type) {
        this(Collections.<Class<?>> singletonList(type));
    }

    public Operator(Class<?>... types) {
        this(Arrays.<Class<?>> asList(types));
    }

    public Operator(List<Class<?>> types) {
        this.types = unmodifiableList(types);
    }

    public List<Class<?>> getTypes() {
        return types;
    }
}