/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotation to be used on Querydsl query types to declare the annotation to be used
 * 
 * @author tiwe
 *
 */
@Documented
@Target(TYPE)
@Retention(RUNTIME)
public @interface Table {
    
    /**
     * @return
     */
    String value();
    
}
