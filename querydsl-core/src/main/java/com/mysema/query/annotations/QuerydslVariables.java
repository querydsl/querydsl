/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.annotations;

import static java.lang.annotation.ElementType.PACKAGE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author tiwe
 *
 */
@Documented
@Target({PACKAGE})
@Retention(RUNTIME)
public @interface QuerydslVariables {
    
    /**
     * @return
     */
    String  value() default "Variables";
    
    /**
     * @return
     */
    boolean asInterface() default false;

}
