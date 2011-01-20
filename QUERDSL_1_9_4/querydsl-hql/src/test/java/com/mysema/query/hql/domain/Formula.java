/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.hql.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * The Class Formula.
 */
@Entity
public class Formula {
    @Id
    int id;

    @ManyToOne
    Parameter parameter;

    public int getId() {
        return id;
    }

    public Parameter getParameter() {
        return parameter;
    }

}
