/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jpa.domain;

import java.io.Serializable;
import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;



/**
 * The Class Calendar.
 */
@SuppressWarnings("serial")
@Entity
public class Calendar implements Serializable{
    @ElementCollection
    @Temporal(TemporalType.DATE)
    Map<String, java.util.Date> holidays;

    @Id
    int id;
}
