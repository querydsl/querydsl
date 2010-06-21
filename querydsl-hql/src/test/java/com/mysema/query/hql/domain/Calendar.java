/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.hql.domain;

import java.io.Serializable;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.CollectionOfElements;

/**
 * The Class Calendar.
 */
@SuppressWarnings("serial")
@Entity
public class Calendar implements Serializable{
    @CollectionOfElements
    Map<String, java.util.Date> holidays;

    @Id
    int id;
}
