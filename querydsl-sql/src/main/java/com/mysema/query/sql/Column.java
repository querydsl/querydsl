package com.mysema.query.sql;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Target({FIELD,METHOD})
@Retention(RUNTIME)
public @interface Column {
    
    /**
     * @return
     */
    String value();

}
