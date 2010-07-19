package com.mysema.query.sql;

import java.lang.annotation.Annotation;

/**
 * @author tiwe
 *
 */
public class ColumnImpl implements Column{

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
