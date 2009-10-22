/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql.domain;

import java.util.List;

import javax.persistence.Id;

import org.hibernate.annotations.CollectionOfElements;

import com.mysema.query.annotations.QueryProjection;

public class FooDTO {
    String bar;
    @Id
    int id;
    @CollectionOfElements
    List<String> names;
    java.util.Date startDate;

    public FooDTO() {
    }

    @QueryProjection
    public FooDTO(long l) {
    }

    @QueryProjection
    public FooDTO(long l, long r) {
    }
}