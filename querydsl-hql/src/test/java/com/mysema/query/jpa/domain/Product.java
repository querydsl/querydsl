/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jpa.domain;

import javax.persistence.Entity;

/**
 * The Class Product.
 */
@Entity
public class Product extends Item {
    // @Id long id;
    String name;
}
