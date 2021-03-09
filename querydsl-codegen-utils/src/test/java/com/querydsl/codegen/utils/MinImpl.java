package com.querydsl.codegen.utils;

import java.lang.annotation.Annotation;

import javax.validation.Payload;
import javax.validation.constraints.*;

/**
 * @author tiwe
 * 
 */
@SuppressWarnings("all")
public class MinImpl implements Min {

    private final long value;

    public MinImpl(long value) {
        this.value = value;
    }

    @Override
    public Class<?>[] groups() {
        return new Class<?>[0];
    }

    @Override
    public String message() {
        return "{javax.validation.constraints.Min.message}";
    }

    @Override
    public Class<? extends Payload>[] payload() {
        return new Class[0];
    }

    @Override
    public long value() {
        return value;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return Min.class;
    }

}
