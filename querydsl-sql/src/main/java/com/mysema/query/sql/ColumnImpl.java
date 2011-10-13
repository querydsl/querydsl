/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql;

import java.lang.annotation.Annotation;


/**
 * ColumnImpl is an implementation for the Column annotation
 * 
 * @author tiwe
 *
 */
@SuppressWarnings("all")
public class ColumnImpl implements Column {

    private final String column;

    public ColumnImpl(String column){
        this.column = column;
    }

    @Override
    public String value() {
        return column;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return Column.class;
    }

}
