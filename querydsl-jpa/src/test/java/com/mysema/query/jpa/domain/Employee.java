/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jpa.domain;

import java.util.Collection;
import java.util.HashSet;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 * The Class Employee.
 */
@Entity
public class Employee {
    @ManyToOne
    public Company company;

    @OneToOne
    public User user;
    
    public String firstName, lastName;

    @Id
    public int id;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "jobfunction")
    @ElementCollection (fetch = FetchType.EAGER)
    public Collection<JobFunction> jobFunctions = new HashSet<JobFunction>();
    
}
