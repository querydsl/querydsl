/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jpa.domain;

import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The Class Foo.
 */
@Entity
public class Foo {
    public String bar;

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    public int id;

    @ElementCollection
    @CollectionTable(name = "foo_names", joinColumns = {@JoinColumn(name="foo_id")})    
    public List<String> names;

    @Temporal(TemporalType.DATE)
    public java.util.Date startDate;
}
