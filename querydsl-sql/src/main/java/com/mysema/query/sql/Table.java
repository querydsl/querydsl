package com.mysema.query.sql;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author tiwe
 *
 */
@Documented
@Target(TYPE)
@Retention(RUNTIME)
public @interface Table {
    
    String value();
    
}
