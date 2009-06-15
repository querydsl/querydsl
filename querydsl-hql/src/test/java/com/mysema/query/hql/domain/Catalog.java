/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql.domain;

import java.util.Date;
import java.util.SortedSet;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;


/**
 * The Class Catalog.
 */
@Entity
public class Catalog {
    Date effectiveDate;
    
    @Id
    int id;
    
    @OneToMany
    @Sort(type = SortType.NATURAL)
    SortedSet<Price> prices;
}