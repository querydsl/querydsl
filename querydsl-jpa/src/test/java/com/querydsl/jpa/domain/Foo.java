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

import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The Class Foo.
 */
@Entity
@Table(name="foo_")
public class Foo {
    public String bar;

    @Id
    //@GeneratedValue(strategy=GenerationType.AUTO)
    public int id;

    @ElementCollection
    @CollectionTable(name = "foo_names", joinColumns = {@JoinColumn(name="foo_id")})    
    public List<String> names;

    @Temporal(TemporalType.DATE)
    public java.util.Date startDate;
}
