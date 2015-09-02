package com.querydsl.codegen;

/**
 * Transforms the case.
 *
 */
public interface CaseTransformer {
    /**
     * Transform the case of the given {@code name}.
     *
     * @param name the string to transform the case of.
     * @return the name with new case
     */
    String transform(String name);
}
