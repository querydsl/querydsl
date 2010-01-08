package com.mysema.query.annotations;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author tiwe
 *
 */
@Documented
@Target({METHOD})
@Retention(RUNTIME)
public @interface QueryMethod {

    /**
     * @return
     */
    String value();
    
}
