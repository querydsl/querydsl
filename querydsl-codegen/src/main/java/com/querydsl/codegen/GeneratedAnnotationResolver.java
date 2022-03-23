package com.querydsl.codegen;

import com.querydsl.core.annotations.Generated;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.util.Date;

import static com.querydsl.codegen.GeneratedAnnotationClass.DEFAULT_CONSTRUCTOR_ARGS;

/**
 * {@code GeneratedAnnotationClassResolver} provides class name resolving functionality for resolving the annotation
 * type to be used on {@link Serializer}s generated sources.
 */
public final class GeneratedAnnotationResolver {

    private static final GeneratedAnnotationClass DEFAULT_GENERATED_ANNOTATION_CLASS = resolveJavaDefault();

    /**
     * The annotation (usually: {@code @Generated}) that will get attached to generated classes.
     * <br>
     * <p>
     * Defaults to <code>javax.annotation.Generated</code> or <code>javax.annotation.processing.Generated</code> depending on the java version.
     * </p>
     * <p>
     * Specify one of:
     * <ul>
     *  <li>null (default)</li>
     *  <li>"" (empty string, same as null)</li>
     *  <li>The fully qualified class name of the <em>Single-Element Annotation</em> (with <code>String</code> element)</li>
     *  <li>A code template in the form <code>fully.qualified.Annotation()</code></li>
     *  <li>or <code>fully.qualified.Annotation("with params")</code></li>
     *  <li>or <code>fully.qualified.Annotation(say="Hello", toWhom="World")</code>.</li>
     * </ul>
     * </p>
     * <p>
     * </p>
     * <p>
     * Parameterized forms also support placeholders for parameters:
     * <ul>
     *     <li>"{@code {{queryDSL.generator.className}}}" - the class name of the generator</li>
     *     <li>"{@code {{queryDSL.timestamp}}}" - A timestamp as returned by {@link Date#getTime()}.</li>
     * </ul>
     * Example: {@code fully.qualified.Annotation("{{queryDSL.generator.className}}")} results in e.g. {@code fully.qualified.Annotation("com.querydsl.sql.codegen.ExtendedBeanSerializer")}
     * </p>
     * <br>
     * <em>See also</em> <a href="https://docs.oracle.com/javase/specs/jls/se8/html/jls-9.html#jls-9.7.3">Single-Element Annotation</a>
     */
    public static GeneratedAnnotationClass resolve(@Nullable String template) {
        if (template != null && !template.isEmpty()) {
            try {
                return new GeneratedAnnotationClass(template);
            } catch (Exception e) {
                // Try next one
            }
        }

        return resolveDefault();
    }

    /**
     * Resolve the java {@code @Generated} annotation (can be of type {@code javax.annotation.Generated}
     * or {@code javax.annotation.processing.Generated} depending on the java version.
     *
     * @return the Generated annotation class from java. Never {@code null}.
     */
    public static GeneratedAnnotationClass resolveDefault() {
        return DEFAULT_GENERATED_ANNOTATION_CLASS;
    }

    public static GeneratedAnnotationClass qeryDSLGenerated() {
        return forSingleValuedAnnotation(Generated.class);
    }

    public static GeneratedAnnotationClass forSingleValuedAnnotation(Class<? extends Annotation> annotationClass) {
        String template = annotationClass.getName() + "(" + DEFAULT_CONSTRUCTOR_ARGS + ")";
        return new GeneratedAnnotationClass(template);
    }

    @SuppressWarnings("unchecked")
    private static GeneratedAnnotationClass resolveJavaDefault() {
        try {
            Class<? extends Annotation> annotationClass = (Class<? extends Annotation>) Class.forName("javax.annotation.processing.Generated");
            return GeneratedAnnotationResolver.forSingleValuedAnnotation(annotationClass);
        } catch (Exception e) {
            // Try next one
        }

        try {
            Class<? extends Annotation> annotationClass = (Class<? extends Annotation>) Class.forName("javax.annotation.Generated");
            return GeneratedAnnotationResolver.forSingleValuedAnnotation(annotationClass);
        } catch (Exception e) {
            // Try next one
        }

        try {
            Class<? extends Annotation> annotationClass = (Class<? extends Annotation>) Class.forName("jakarta.annotation.Generated");
            return GeneratedAnnotationResolver.forSingleValuedAnnotation(annotationClass);
        } catch (Exception e) {
            // Try next one
        }

        throw new IllegalStateException("Can't find Generated annotation");
    }

    private GeneratedAnnotationResolver() { }
}
