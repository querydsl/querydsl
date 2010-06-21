/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.hql.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The Class Bar.
 */
@Entity
public class Bar {
    @Temporal(TemporalType.DATE)
    java.util.Date date;

    @Id
    int id;
}
