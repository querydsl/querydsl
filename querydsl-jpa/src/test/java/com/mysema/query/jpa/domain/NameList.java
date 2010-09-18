/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jpa.domain;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.CollectionOfElements;

/**
 * The Class NameList.
 */
@Entity
public class NameList {
    @Id
    long id;
    @CollectionOfElements
    Collection<String> names;
}
