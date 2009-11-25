/*
 * Copyright (c) 2009 Mysema Ltd.
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
 * Annotate fields and getters that should be skipped in APT based code generation
 * with this annotation
 * 
 * @author tiwe
 *
 */
@Documented
@Target({FIELD,METHOD})
@Retention(RUNTIME)
public @interface QueryTransient {

}
