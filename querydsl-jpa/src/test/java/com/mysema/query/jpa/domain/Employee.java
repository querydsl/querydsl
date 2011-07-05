/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jpa.domain;

import java.util.Collection;
import java.util.HashSet;

import javax.persistence.*;

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
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    public int id;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "jobfunction")
    @ElementCollection (fetch = FetchType.EAGER)
    public Collection<JobFunction> jobFunctions = new HashSet<JobFunction>();
    
}
