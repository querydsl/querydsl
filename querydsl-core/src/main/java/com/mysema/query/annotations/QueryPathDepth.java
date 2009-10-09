package com.mysema.query.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Overrides the default path initialization depth for this property.
 * 
 * @author tiwe
 *
 */
@Documented
@Target({FIELD,METHOD})
@Retention(RUNTIME)
public @interface QueryPathDepth {
    
    int value();

}
