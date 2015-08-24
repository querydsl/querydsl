package com.mysema.codegen;

import java.lang.annotation.Annotation;

import javax.validation.ConstraintPayload;
import javax.validation.constraints.*;

/**
 * @author tiwe
 * 
 */
@SuppressWarnings("all")
public class NotNullImpl implements NotNull {

    @Override
    public Class<?>[] groups() {
        return new Class<?>[0];
    }

    @Override
    public String message() {
        return "{javax.validation.constraints.NotNull.message}";
    }

    @Override
    @SuppressWarnings("unchecked") // Empty array
    public Class<? extends ConstraintPayload>[] payload() {
        return (Class<? extends ConstraintPayload>[]) new Class<?>[0];
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return NotNull.class;
    }

}
