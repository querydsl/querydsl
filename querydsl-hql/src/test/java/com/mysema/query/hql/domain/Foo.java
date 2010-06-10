/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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
    
    @Temporal(TemporalType.DATE)
    java.util.Date startDate;
}