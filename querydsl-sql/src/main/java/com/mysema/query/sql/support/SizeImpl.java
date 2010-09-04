/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql.support;

import java.lang.annotation.Annotation;

import javax.validation.ConstraintPayload;
import javax.validation.constraints.Size;

/**
 * @author tiwe
 *
 */
@SuppressWarnings("all")
public class SizeImpl implements Size {
    
    private final int min, max;
    
    public SizeImpl(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public Class<?>[] groups() {
        return new Class[0];
    }

    @Override
    public String message() {
        return "{javax.validation.constraints.Size.message}";
    }

    @Override
    public Class<? extends ConstraintPayload>[] payload() {
        return new Class[0];
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return Size.class;
    }

    @Override
    public int max() {
        return max;
    }

    @Override
    public int min() {
        return min;
    }

}
