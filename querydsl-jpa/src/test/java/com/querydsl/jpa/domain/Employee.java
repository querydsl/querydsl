/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.jpa.domain;

import java.util.Collection;
import java.util.HashSet;

import javax.persistence.*;

/**
 * The Class Employee.
 */
@Entity
@Table(name="employee_")
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
