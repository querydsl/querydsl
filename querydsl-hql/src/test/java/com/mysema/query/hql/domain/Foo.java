/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.IndexColumn;

/**
 * The Class Foo.
 */
@Entity
public class Foo {
    String bar;
    @Id
    int id;
    @CollectionOfElements
    @IndexColumn(name = "_index")
    List<String> names;
    java.util.Date startDate;
}