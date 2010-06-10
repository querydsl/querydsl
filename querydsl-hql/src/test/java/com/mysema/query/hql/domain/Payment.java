/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql.domain;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 * The Class Payment.
 */
@Entity
public class Payment extends Item {
    @ManyToOne
    Status currentStatus, status;
    
    PaymentStatus name;
    
    @OneToMany
    Collection<StatusChange> statusChanges;
}