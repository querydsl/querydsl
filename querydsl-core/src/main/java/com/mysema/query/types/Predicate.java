/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types;

/**
 * @author tiwe
 *
 */
public interface Predicate extends Expression<Boolean>{
    
    /**
     * @return
     */
    Predicate not();

}
