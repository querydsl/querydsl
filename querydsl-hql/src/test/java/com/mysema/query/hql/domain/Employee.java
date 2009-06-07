/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * The Class Employee.
 */
@Entity
public class Employee {
    @ManyToOne
    Company company;
    String firstName, lastName;
    @Id
    int id;
}