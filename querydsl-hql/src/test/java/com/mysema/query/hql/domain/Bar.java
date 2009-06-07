/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * The Class Bar.
 */
@Entity
public class Bar {
    java.util.Date date;
    @Id
    int id;
}