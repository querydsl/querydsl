package com.querydsl.codegen;

import org.junit.Test;

import static org.junit.Assert.*;

public class GeneratedAnnotationResolverTest {

    private final GeneratedAnnotationClass.Context ctx = GeneratedAnnotationClass.Context.of("my.GeneratorBean");

    @Test
    public void resolveCustom() {
        String customClass = "some.random.Class";
        GeneratedAnnotationClass resolvedAnnotationClass = GeneratedAnnotationResolver.resolve(customClass);
        assertNotNull(resolvedAnnotationClass);
        assertEquals("some.random", resolvedAnnotationClass.getPackageName());
        assertEquals("Class", resolvedAnnotationClass.getSimpleName());
        assertEquals("\"{{queryDSL.generator.className}}\"", resolvedAnnotationClass.getConstructorArgs());
        assertEquals("\"my.GeneratorBean\"", resolvedAnnotationClass.buildAnnotationArgs(ctx));
        assertEquals("Class(\"my.GeneratorBean\")", resolvedAnnotationClass.buildAnnotation(ctx));
    }

    @Test
    public void resolveNull() {
        GeneratedAnnotationClass  resolvedAnnotationClass = GeneratedAnnotationResolver.resolve(null);
        assertNotNull(resolvedAnnotationClass);
        assertEquals("javax.annotation.processing", resolvedAnnotationClass.getPackageName());
        assertEquals("Generated", resolvedAnnotationClass.getSimpleName());
        assertEquals("\"{{queryDSL.generator.className}}\"", resolvedAnnotationClass.getConstructorArgs());
        assertEquals("\"my.GeneratorBean\"", resolvedAnnotationClass.buildAnnotationArgs(ctx));
        assertEquals("Generated(\"my.GeneratorBean\")", resolvedAnnotationClass.buildAnnotation(ctx));
    }

    @Test
    public void resolveDefault() {
        GeneratedAnnotationClass resolvedAnnotationClass = GeneratedAnnotationResolver.resolveDefault();
        assertNotNull(resolvedAnnotationClass);
        assertEquals("javax.annotation.processing", resolvedAnnotationClass.getPackageName());
        assertEquals("Generated", resolvedAnnotationClass.getSimpleName());
        assertEquals("\"{{queryDSL.generator.className}}\"", resolvedAnnotationClass.getConstructorArgs());
        assertEquals("\"my.GeneratorBean\"", resolvedAnnotationClass.buildAnnotationArgs(ctx));
        assertEquals("Generated(\"my.GeneratorBean\")", resolvedAnnotationClass.buildAnnotation(ctx));
    }

    @Test
    public void parse_template_without_params() {
        String template = "foo.bar.Banana";

        GeneratedAnnotationClass resolved = GeneratedAnnotationResolver.resolve(template);

        assertEquals("foo.bar", resolved.getPackageName());
        assertEquals("Banana", resolved.getSimpleName());
        assertEquals("\"{{queryDSL.generator.className}}\"", resolved.getConstructorArgs());
        assertEquals("\"my.GeneratorBean\"", resolved.buildAnnotationArgs(ctx));
        assertEquals("Banana(\"my.GeneratorBean\")", resolved.buildAnnotation(ctx));
    }


    @Test
    public void parse_template_with_params() {
        String template = "foo.bar.MyGenerated(\"{{queryDSL.generator.className}}\")";

        GeneratedAnnotationClass resolved = GeneratedAnnotationResolver.resolve(template);

        assertEquals("foo.bar", resolved.getPackageName());
        assertEquals("MyGenerated", resolved.getSimpleName());
        assertEquals("\"{{queryDSL.generator.className}}\"", resolved.getConstructorArgs());
        assertEquals("\"my.GeneratorBean\"", resolved.buildAnnotationArgs(ctx));
        assertEquals("MyGenerated(\"my.GeneratorBean\")", resolved.buildAnnotation(ctx));
    }

    @Test
    public void parse_template_with_empty_params() {
        String template = "foo.bar.MyGenerated()";

        GeneratedAnnotationClass resolved = GeneratedAnnotationResolver.resolve(template);

        assertEquals("foo.bar", resolved.getPackageName());
        assertEquals("MyGenerated", resolved.getSimpleName());
        assertEquals("", resolved.getConstructorArgs());
        assertEquals("", resolved.buildAnnotationArgs(ctx));
        assertEquals("MyGenerated", resolved.buildAnnotation(ctx));
    }

    @Test
    public void parse_template_with_many_params() {
        String template = "foo.bar.MyGenerated(value=\"{{queryDSL.generator.className}}\", description=\"testing\")";

        GeneratedAnnotationClass resolved = GeneratedAnnotationResolver.resolve(template);

        assertEquals("foo.bar", resolved.getPackageName());
        assertEquals("MyGenerated", resolved.getSimpleName());
        assertEquals("value=\"{{queryDSL.generator.className}}\", description=\"testing\"", resolved.getConstructorArgs());
        assertEquals("value=\"my.GeneratorBean\", description=\"testing\"", resolved.buildAnnotationArgs(ctx));
        assertEquals("MyGenerated(value=\"my.GeneratorBean\", description=\"testing\")", resolved.buildAnnotation(ctx));
    }

    @Test
    public void template_using_timestamp_placeholder() {
        String template = "foo.bar.MyGenerated(timestamp=\"{{queryDSL.timestamp}}\")";

        GeneratedAnnotationClass resolved = GeneratedAnnotationResolver.resolve(template);
        String actual = resolved.buildAnnotation(ctx);

        assertTrue(actual.matches(".*timestamp=\"\\d{13,}\".*"));
    }


}
