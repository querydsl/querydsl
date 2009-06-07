/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * The Class Document.
 */
@Entity
public class Document {
    @Id
    int id;
    String name;
    Date validTo;
}