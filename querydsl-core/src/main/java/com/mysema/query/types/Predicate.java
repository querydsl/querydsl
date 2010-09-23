/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types;

/**
 * Predicate is the common interface for Boolean typed expressions
 * 
 * @author tiwe
 *
 */
public interface Predicate extends Expression<Boolean>{
    
    /**
     * Get the negation of the expression
     * 
     * @return
     */
    Predicate not();

}
