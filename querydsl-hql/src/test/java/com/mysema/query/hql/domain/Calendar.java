/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql.domain;

import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.CollectionOfElements;

/**
 * The Class Calendar.
 */
@Entity
public class Calendar {
    @CollectionOfElements
    Map<String, java.util.Date> holidays;
    
    @Id
    int id;
}