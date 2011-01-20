/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.hql.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * The Class EvilType.
 */
@Entity
public class EvilType {
    @ManyToOne
    @JoinColumn(name = "_asc")
    EvilType asc;

    @ManyToOne
    @JoinColumn(name = "_desc")
    EvilType desc;

    @Id
    int id;

    @ManyToOne
    EvilType isnull, isnotnull, get, getType, getMetadata;

    @ManyToOne
    EvilType toString, hashCode, getClass, notify, notifyAll, wait;
}
