/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.util;


/**
 * SimpleEvaluator is a Java Compiler API based implementation of the Evaluator interface
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

}
