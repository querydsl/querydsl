/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;


/**
 * The Class Account.
 */
@Entity
public class Account {
    @Id
    long id;
    @ManyToOne
    Person owner;
    @Embedded
    EmbeddedType embeddedData;
}