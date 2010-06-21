/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Declaration of extension methods on an external type. Extensions of builtin types (e.g. String, Boolean, JDK Number types)
 * are not supported
 *
 * @author tiwe
 *
 */
@Documented
@Target(TYPE)
@Retention(RUNTIME)
// TODO : rename to Extensions
public @interface QueryExtensions {

    /**
     * Type to be extended
     *
     * @return
     */
    Class<?> value();

}
