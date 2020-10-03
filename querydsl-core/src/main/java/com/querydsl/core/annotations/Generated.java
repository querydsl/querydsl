package com.querydsl.core.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.CLASS;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * The Generated annotation is used to mark source code that has been generated.
 * It uses <code>CLASS</code> retention to allow byte code analysis tools like
 * Jacoco detect this code as being generated.
 * <p/>
 * It is now used instead of <code>javax.annotation.Generated</code>.
 */

@Documented
@Retention(CLASS)
@Target(TYPE)
public @interface Generated {
    /**
     * The value element MUST have the name of the code generator.
     * The recommended convention is to use the fully qualified name of the
     * code generator. For example: com.acme.generator.CodeGen.
     */
    String[] value();
}
