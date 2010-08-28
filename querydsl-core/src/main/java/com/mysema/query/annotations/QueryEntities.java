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
 * Annotation for APT based Domain query type generation. Annotate Domain packages with this annotation.
 * 
 * @author tiwe
 *
 */
@Documented
@Target(PACKAGE)
@Retention(RUNTIME)
public @interface QueryEntities {

    /**
     * @return
     */
    Class<?>[] value();
}
