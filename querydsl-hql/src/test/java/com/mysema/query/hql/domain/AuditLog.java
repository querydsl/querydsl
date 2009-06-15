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
 * The Class AuditLog.
 */
@Entity
public class AuditLog {
    @Id
    int id;
    
    @ManyToOne
    Item item;
}