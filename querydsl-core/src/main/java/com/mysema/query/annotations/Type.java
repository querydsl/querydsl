package com.mysema.query.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.mysema.query.types.TypeCategory;

@Documented
@Target({FIELD,METHOD})
@Retention(RUNTIME)
public @interface Type {

    TypeCategory value();
}
