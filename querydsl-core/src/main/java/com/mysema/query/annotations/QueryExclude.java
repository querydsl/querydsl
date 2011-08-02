package com.mysema.query.annotations;

import static java.lang.annotation.ElementType.PACKAGE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Marks the annotated classes and packages to be excluded from source generation
 * 
 * @author tiwe
 *
 */
@Documented
@Target({TYPE, PACKAGE})
@Retention(RUNTIME)
public @interface QueryExclude {

}
