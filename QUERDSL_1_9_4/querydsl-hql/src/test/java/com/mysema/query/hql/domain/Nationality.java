/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.hql.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * The Class Nationality.
 */
@SuppressWarnings("serial")
@Entity
public class Nationality implements Serializable{
    @ManyToOne
    Calendar calendar;

    @Id
    long id;
}
