/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.hql.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.IndexColumn;

/**
 * The Class Company.
 */
@Entity
public class Company {
    @ManyToOne
    Employee ceo;

    @OneToMany
    @IndexColumn(name = "_index")
    List<Department> departments;

    @Id
    int id;

    String name;
}
