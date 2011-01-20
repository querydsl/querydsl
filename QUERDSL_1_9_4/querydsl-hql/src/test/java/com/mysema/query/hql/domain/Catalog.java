/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.hql.domain;

import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The Class Catalog.
 */
@Entity
public class Catalog {
    @Temporal(TemporalType.DATE)
    Date effectiveDate;

    @Id
    int id;

//    @OneToMany
//    @Sort(type = SortType.NATURAL)
//    SortedSet<Price> prices;

    @OneToMany
    Set<Price> prices;
}
