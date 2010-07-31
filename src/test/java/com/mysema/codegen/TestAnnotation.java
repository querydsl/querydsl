package com.mysema.codegen;

import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target( { TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface TestAnnotation {
    
    boolean prop1() default false;
    
    boolean prop2();

}
