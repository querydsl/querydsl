package com.querydsl.codegen;

import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;

/**
 * {@code GeneratedAnnotationClassResolver} provides class name resolving functionality for resolving the annotation
 * type to be used on {@link Serializer}s generated sources.
 */
public final class GeneratedAnnotationResolver {

    private static final Class<? extends Annotation> DEFAULT_GENERATED_ANNOTATION_CLASS = resolveJavaDefault();

    /**
     * Use the {@code generatedAnnotationClass} or use the JDK one.
     * <p>
     * A {@code null generatedAnnotationClass} will resolve to the java {@code @Generated} annotation (can be of type {@code javax.annotation.Generated}
     * or {@code javax.annotation.processing.Generated} depending on the java version.
     *
     * @param generatedAnnotationClass the fully qualified class name of the <em>Single-Element Annotation</em> (with {@code String} element)
     *                                 to use or {@code null}.
     * @return the provided {@code generatedAnnotationClass} if not {@code null} or the one from java. Never {@code null}.
     * @see <a href="https://docs.oracle.com/javase/specs/jls/se8/html/jls-9.html#jls-9.7.3">Single-Element Annotation</a>
     */
    public static Class<?extends Annotation> resolve(@Nullable String generatedAnnotationClass) {
        if (generatedAnnotationClass != null) {
            try {
                return (Class<? extends Annotation>) Class.forName(generatedAnnotationClass);
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
    public static Class<? extends Annotation> resolveDefault() {
        return DEFAULT_GENERATED_ANNOTATION_CLASS;
    }

    @SuppressWarnings("unchecked")
    private static Class<? extends Annotation> resolveJavaDefault() {
        try {
            return (Class<? extends Annotation>) Class.forName("javax.annotation.processing.Generated");
        } catch (Exception e) {
            // Try next one
        }

        try {
            return (Class<? extends Annotation>) Class.forName("javax.annotation.Generated");
        } catch (Exception e) {
            // Try next one
        }

        try {
            return (Class<? extends Annotation>) Class.forName("jakarta.annotation.Generated");
        } catch (Exception e) {
            // Try next one
        }

        throw new IllegalStateException("Can't find Generated annotation");
    }

    private GeneratedAnnotationResolver() { }
}
