/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.mysema.query.annotations.QueryInit;


/**
 * The Class Person.
 */
@SuppressWarnings("serial")
@Entity
public class Person implements Serializable{
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