/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.annotations;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.CONSTRUCTOR)
@Retention(RUNTIME)
/**
 * Annotation for APT based DTO query type generation. Annotate DTO types with this annotation.
 */
public @interface QueryProjection {
}