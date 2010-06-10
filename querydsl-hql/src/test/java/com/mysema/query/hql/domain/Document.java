/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The Class Document.
 */
@Entity
public class Document {
    @Id
    int id;
    
    String name;
    
    @Temporal(TemporalType.DATE)
    Date validTo;
}