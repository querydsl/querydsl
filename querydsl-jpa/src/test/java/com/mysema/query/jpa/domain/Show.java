/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jpa.domain;

import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.CollectionOfElements;

/**
 * The Class Show.
 */
@Entity
public class Show {
    @CollectionOfElements
    public Map<String, String> acts;

    @Id
    public int id;
}
