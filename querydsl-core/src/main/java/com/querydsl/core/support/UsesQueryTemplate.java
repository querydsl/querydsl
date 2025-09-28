package com.querydsl.core.support;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that a method uses a QueryTemplate.
 * <p>
 * This annotation is used to mark methods that utilize a QueryTemplate for their execution.
 * QueryTemplates are a way to abstract and simplify complex queries.
 * <p>
 * This annotation is retained at runtime and can be used for reflection-based analysis.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface UsesQueryTemplate {
}