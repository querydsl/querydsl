/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The Class StatusChange.
 */
@Entity
public class StatusChange {
    @Id
    long id;
    
    @Temporal(TemporalType.TIMESTAMP)
    java.util.Date timeStamp;
}