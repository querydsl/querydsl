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

/**
 * QuerydslConfig defines serialization flags for annotated domain types and packages
 * 
 * @author tiwe
 *
 */
@Documented
@Target({PACKAGE,TYPE})
@Retention(RUNTIME)
public @interface QuerydslConfig {
    
    /**
     * Created entity field initialization accessors
     * 
     * @return
     */
    boolean entityAccessors() default false;
    
    /**
     * Create accessors for indexed list access
     * 
     * @return
     */
    boolean listAccessors() default false;
        
    /**
     * Create accessors for keyed map access
     * 
     * @return
     */
    boolean mapAccessors() default false;
    
    /**
     * Create default variable in query type
     * 
     * @return
     */
    boolean createDefaultVariable() default true;

}
