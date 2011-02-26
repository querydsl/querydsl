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

/**
 * The Class Calendar.
 */
@SuppressWarnings("serial")
@Entity
public class Calendar implements Serializable{
    @ElementCollection
    Map<String, java.util.Date> holidays;

    @Id
    int id;
}
