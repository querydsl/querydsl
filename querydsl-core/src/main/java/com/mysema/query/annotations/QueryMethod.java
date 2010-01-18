/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.annotations;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Declaration of an extension method
 * 
 * e.g.
 * <pre>
 * @QueryMethod("{0}.myMethod({1})")
 * public int myMethod(String arg);
 * </pre>
 * 
 * @author tiwe
 *
 */
@Documented
@Target({METHOD})
@Retention(RUNTIME)
public @interface QueryMethod {

    /**
     * Querydsl serialization template for extension method
     * 
     * 
     * @return
     */
    String value();
    
}
