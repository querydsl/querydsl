package com.querydsl.codegen;

import static org.junit.Assert.assertEquals;

import javax.annotation.Generated;

import org.junit.Test;

public class GeneratedAnnotationResolverTest {

    private static final String defaultGenerated = Generated.class.getName();

    @Test
    public void resolveCustom() {
        String customClass = "some.random.Class";
        String resolvedAnnotationClass = GeneratedAnnotationResolver.resolve(customClass);
        assertEquals(customClass, resolvedAnnotationClass);
    }

    @Test
    public void resolveNull() {
        String resolvedAnnotationClass = GeneratedAnnotationResolver.resolve(null);
        assertEquals(defaultGenerated, resolvedAnnotationClass);
    }

    @Test
    public void resolveDefault() {
        String resolvedAnnotationClass = GeneratedAnnotationResolver.resolveDefault();
        assertEquals(defaultGenerated, resolvedAnnotationClass);
    }
}
