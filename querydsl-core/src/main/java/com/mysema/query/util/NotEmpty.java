package com.mysema.query.util;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.annotation.MatchesPattern;
import javax.annotation.meta.TypeQualifierNickname;
import javax.annotation.meta.When;

/**
 * NotEmpty provides
 *
 * @author tiwe
 * @version $Id$
 */
@Documented
@TypeQualifierNickname 
@MatchesPattern(".+")
@Retention(RetentionPolicy.RUNTIME)
public @interface NotEmpty {
    When when() default When.ALWAYS;
}
