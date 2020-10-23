package com.querydsl.codegen;

import javax.annotation.Nullable;

/**
 * {@code GeneratedAnnotationClassResolver} provides class name resolving functionality for resolving the annotation
 * type to be used on {@link Serializer}s generated sources.
 */
public final class GeneratedAnnotationResolver {
    private static final String DEFAULT_GENERATED_ANNOTATION_CLASS = resolveJavaDefault();

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
    public static String resolve(@Nullable String generatedAnnotationClass) {
        if (generatedAnnotationClass != null) {
            return generatedAnnotationClass;
        }
        return DEFAULT_GENERATED_ANNOTATION_CLASS;
    }

    /**
     * Resolve the java {@code @Generated} annotation (can be of type {@code javax.annotation.Generated}
     * or {@code javax.annotation.processing.Generated} depending on the java version.
     *
     * @return the Generated annotation class from java. Never {@code null}.
     */
    public static String resolveDefault() {
        return DEFAULT_GENERATED_ANNOTATION_CLASS;
    }

    private static String resolveJavaDefault() {
        try {
            return Class.forName("javax.annotation.processing.Generated").getName();
        } catch (Throwable java9GeneratedNotFound) {
            return "javax.annotation.Generated";
        }
    }

    private GeneratedAnnotationResolver() { }
}
