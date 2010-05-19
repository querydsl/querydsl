/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * The Class Price.
 */
@Entity
public class Price{
    long amount;
    
    @Id
    long id;
    
    @ManyToOne
    Product product;

}