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
public class NotNullImpl implements NotNull{

    @Override
    public Class<?>[] groups() {
        return new Class[0];
    }

    @Override
    public String message() {
        return "{javax.validation.constraints.NotNull.message}";
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return NotNull.class;
    }

    @Override
    public Class<? extends Payload>[] payload() {
        return new Class[0];
    }

}
