/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Defines the paths to be initialized for the annotated property
 * 
 * @author tiwe
 *
 */
@Documented
@Target({FIELD,METHOD})
@Retention(RUNTIME)
public @interface QueryInit {

    /**
     * Initialized paths
     * 
     * @return
     */
    String[] value();
    
}
