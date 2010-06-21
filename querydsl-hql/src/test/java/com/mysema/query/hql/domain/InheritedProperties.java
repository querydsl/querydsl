/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.hql.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class InheritedProperties extends Superclass {
    @Id
    long id;

    String classProperty;
}
