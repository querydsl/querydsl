/*
 * Copyright 2011, Mysema Ltd
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

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.querydsl.core.annotations.QueryInit;

/**
 * The Class Person.
 */
@SuppressWarnings("serial")
@Entity
@Table(name="person_")
public class Person implements Serializable{
    @Temporal(TemporalType.DATE)
    java.util.Date birthDay;

    @Id
    long i;

    @ManyToOne
    PersonId pid;

    String name;

    @ManyToOne
    @QueryInit("calendar")
    Nationality nationality;
}
