/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql;

import java.lang.annotation.Annotation;

/**
 * @author tiwe
 *
 */
public class TableImpl implements Table{

    private final String table;
    
    public TableImpl(String table){
        this.table = table;
    }
    
    @Override
    public String value() {
        return table;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return Table.class;
    }

}
