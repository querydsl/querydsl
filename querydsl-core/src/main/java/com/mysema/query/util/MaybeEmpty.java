/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.util;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.annotation.meta.TypeQualifierNickname;
import javax.annotation.meta.When;

/**
 * MaybeEmpty provides
 *
 * @author tiwe
 * @version $Id$
 */
@Documented
@Retention(value=RetentionPolicy.RUNTIME)
@TypeQualifierNickname 
@NotEmpty(when=When.MAYBE)
public @interface MaybeEmpty {

}
