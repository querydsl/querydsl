/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql;

import java.lang.annotation.Annotation;


/**
 * TableImpl is an implementation for the Table annotation
 *
 * @author tiwe
 *
 */
@SuppressWarnings("all")
public class SchemaImpl implements Schema{

    private final String table;

    public SchemaImpl(String table){
        this.table = table;
    }

    @Override
    public String value() {
        return table;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return Schema.class;
    }

}
