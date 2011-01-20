/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.hql.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * The Class DomesticCat.
 */
@Entity
@DiscriminatorValue("DC")
public class DomesticCat extends Cat {

}
