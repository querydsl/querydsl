/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jpa.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * The Class AuditLog.
 */
@Entity
public class AuditLog {
    @Id
    int id;

    @ManyToOne
    Item item;
}
