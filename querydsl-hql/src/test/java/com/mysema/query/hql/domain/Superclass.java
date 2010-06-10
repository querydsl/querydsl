/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql.domain;

import javax.persistence.MappedSuperclass;

import com.mysema.query.annotations.PropertyType;
import com.mysema.query.annotations.QueryType;

@MappedSuperclass
public class Superclass {
    String superclassProperty;
    
    @QueryType(PropertyType.SIMPLE)
    private String stringAsSimple;

    public String getStringAsSimple() {
        return stringAsSimple;
    }

    public void setStringAsSimple(String stringAsSimple) {
        this.stringAsSimple = stringAsSimple;
    }
    
    
}