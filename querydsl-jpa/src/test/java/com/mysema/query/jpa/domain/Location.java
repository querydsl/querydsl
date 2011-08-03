/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jpa.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * The Class Location.
 */
@Entity(name="Location2")
public class Location {
    @Id
    long id;

    String name;
}
