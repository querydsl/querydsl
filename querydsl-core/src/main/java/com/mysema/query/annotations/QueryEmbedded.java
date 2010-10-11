package com.mysema.query.annotations;

import java.lang.annotation.ElementType;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RUNTIME)
/**
 * Annotation for APT based Domain query type generation. Annotate embdded properties with this annotation.
 */
public @interface QueryEmbedded {

}
