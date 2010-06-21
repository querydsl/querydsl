/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.hql.domain;

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
    Map<String, String> acts;

    @Id
    int id;
}
