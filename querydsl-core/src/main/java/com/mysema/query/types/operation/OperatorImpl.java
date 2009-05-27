package com.mysema.query.types.operation;

import static java.util.Collections.unmodifiableList;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * The Class Op represents an Operator symbol.
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

    /* (non-Javadoc)
     * @see com.mysema.query.types.operation.Operator#getTypes()
     */
    public List<Class<?>> getTypes() {
        return types;
    }
}