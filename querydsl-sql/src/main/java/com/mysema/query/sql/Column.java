/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Defines the related SQL table column for a property
 * 
 * @author tiwe
 *
 */
@Documented
@Target({FIELD,METHOD})
@Retention(RUNTIME)
public @interface Column {
    
    /**
     * @return column name
     */
    String value();

}
