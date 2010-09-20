/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql.support;

import java.lang.annotation.Annotation;

import javax.validation.Payload;
import javax.validation.constraints.*;

/**
 * @author tiwe
 *
 */
@SuppressWarnings("all")
public class MaxImpl implements Max{
    
    private final long value;
    
    public MaxImpl(long value) {
        this.value = value;
    }

    @Override
    public Class<?>[] groups() {
        return new Class[0];
    }

    @Override
    public String message() {
        return "{javax.validation.constraints.Max.message}";
    }

    @Override
    public long value() {
        return value;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return Max.class;
    }

    @Override
    public Class<? extends Payload>[] payload() {
        return new Class[0];
    }

}
