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
 * The Class Department.
 */
@Entity
public class Department {
    @ManyToOne
    Company company;

    @OneToMany
    @IndexColumn(name = "_index")
    List<Employee> employees;

    @Id
    int id;
    String name;
}
