package com.mysema.query.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target(TYPE) 
@Retention(RUNTIME)

/**
 * Annotion for APT based Domain query type generation. 
 * Annotate Domain types with this annotation.
 */
public @interface Literal {

}
