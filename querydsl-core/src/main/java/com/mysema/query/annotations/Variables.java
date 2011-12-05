/*
 * Copyright (c) 2010 Mysema Ltd.
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
 * QuerydslVariables defines that a Variables class or interface is to be serialized into the host package.
 * The type will contain public static final constants for the entity types of the package.
 *
 * @author tiwe
 * 
 * @deprecated will be removed soon
 *
 */
@Documented
@Target({PACKAGE})
@Retention(RUNTIME)
@Deprecated
public @interface Variables {

    /**
     * Simple name of the variables class
     *
     * @return
     */
    String  value() default "Variables";

    /**
     * Generate interface instead of class
     *
     * @return
     */
    boolean asInterface() default false;

}
