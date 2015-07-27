package com.querydsl.jpa;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Inherited
public @interface NoBatooJPA {

    com.querydsl.core.Target[] value() default {};
}
