/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.codegen;

/**
 * Evaluator defines an interface for returning a value as a result of
 * evaluating an expression using the given argument array
 * 
 * @author tiwe
 * 
 */
public interface Evaluator<T> {

    /**
     * @param args
     * @return
     */
    T evaluate(Object... args);

    /**
     * @return
     */
    Class<? extends T> getType();

}
