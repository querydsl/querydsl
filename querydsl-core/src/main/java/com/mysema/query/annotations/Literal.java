/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.annotations;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Target({TYPE,FIELD,METHOD})
@Retention(RUNTIME)
/**
 * Annotation for APT based Domain query type generation. Annotate Literal types with this annotation.
 */
public @interface Literal {

}
