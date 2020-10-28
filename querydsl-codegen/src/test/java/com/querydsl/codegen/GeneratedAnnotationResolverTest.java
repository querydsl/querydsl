package com.querydsl.codegen;

import org.junit.Test;

import javax.annotation.Generated;
import java.lang.annotation.Annotation;

import static org.junit.Assert.assertNotNull;

public class GeneratedAnnotationResolverTest {

    private static final String defaultGenerated = Generated.class.getName();

    @Test
    public void resolveCustom() {
        String customClass = "some.random.Class";
        Class<? extends Annotation> resolvedAnnotationClass = GeneratedAnnotationResolver.resolve(customClass);
        assertNotNull(resolvedAnnotationClass);
    }

    @Test
    public void resolveNull() {
        Class<? extends Annotation>  resolvedAnnotationClass = GeneratedAnnotationResolver.resolve(null);
        assertNotNull(resolvedAnnotationClass);
    }

    @Test
    public void resolveDefault() {
        Class<? extends Annotation> resolvedAnnotationClass = GeneratedAnnotationResolver.resolveDefault();
        assertNotNull(resolvedAnnotationClass);
    }
}
