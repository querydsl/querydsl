/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jpa.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.mysema.query.annotations.QueryInit;

/**
 * The Class Person.
 */
@SuppressWarnings("serial")
@Entity
public class Person implements Serializable{
    @Temporal(TemporalType.DATE)
    java.util.Date birthDay;

    @Id
    long i;

    @ManyToOne
    PersonId pid;

    String name;

    @ManyToOne
    @QueryInit("calendar")
    Nationality nationality;
}
