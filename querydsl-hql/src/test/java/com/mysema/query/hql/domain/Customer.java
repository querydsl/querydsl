/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;


/**
 * The Class Customer.
 */
@Entity
public class Customer {
    @ManyToOne
    Order currentOrder;
    
    @Id
    int id;
    
    @ManyToOne
    Name name;
}