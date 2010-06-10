/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * The Class PersonId.
 */
@SuppressWarnings("serial")
@Entity
public class PersonId implements Serializable{
    String country;
    
    @Id
    long id;
    
    int medicareNumber;
}