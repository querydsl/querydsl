/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.domain;

import java.util.List;

import com.mysema.query.annotations.QueryEntity;

@QueryEntity
public interface InterfaceType {
    InterfaceType getRelation();

    List<InterfaceType> getRelation2();

    List<? extends InterfaceType> getRelation3();

    int getRelation4();
}