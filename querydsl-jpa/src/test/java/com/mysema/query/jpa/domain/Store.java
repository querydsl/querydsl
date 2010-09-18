/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jpa.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 * The Class Store.
 */
@Entity
public class Store {
    @OneToMany
    List<Customer> customers;

    @Id
    long id;

    @ManyToOne
    Location location;
}
