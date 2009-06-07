/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * The Class PersonId.
 */
@Entity
public class PersonId {
    String country;
    @Id
    long id;
    int medicareNumber;
}