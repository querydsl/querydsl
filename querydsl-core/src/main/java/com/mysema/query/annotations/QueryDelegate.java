/*
 * Copyright (c) 2010 Mysema Ltd.
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
 * Declaration of a static delegate method
 *
 * @author tiwe
 *
 */
@Documented
@Target({METHOD})
@Retention(RUNTIME)
public @interface QueryDelegate {

    /**
     * @return
     */
    Class<?> value();

}
