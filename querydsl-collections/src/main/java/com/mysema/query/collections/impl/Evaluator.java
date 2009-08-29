/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections.impl;


/**
 * Evaluator defines an interface for evaluating Querydsl expressions
 * 
 * @author tiwe
 * @version $Id$
 */
public interface Evaluator<T> {
    
    /**
     * @param args
     * @return
     */
    T evaluate(Object... args);

}
