/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jpa.domain;

import java.util.Collection;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * The Class NameList.
 */
@Entity
public class NameList {
    @Id
    long id;
    @ElementCollection
    Collection<String> names;
}
