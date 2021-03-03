/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.querydsl.codegen.utils;

import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Annotation {

    boolean prop1() default false;

    boolean prop2();

    Class<?> clazz();

}
