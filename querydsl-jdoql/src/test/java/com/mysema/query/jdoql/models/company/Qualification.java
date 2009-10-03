/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.jdoql.models.company;

import java.util.Date;

import com.mysema.query.annotations.QueryEntity;

/**
 * Qualification of a person.
 * 
 * @version $Revision: 1.1 $
 */
@QueryEntity
public class Qualification {
    private Person person;
    private String name;
    private Organisation organisation;
    private Date date;

    /**
     * Default constructor required since this is a PersistenceCapable class.
     */
    public Qualification() {
    }

    public Qualification(String name) {
        this.name = name;
    }

    public void setPerson(Person mgr) {
        this.person = mgr;
    }

    public Person getPerson() {
        return this.person;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return this.date;
    }

    public void setOrganisation(Organisation org) {
        this.organisation = org;
    }

    public Organisation getOrganisation() {
        return this.organisation;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return "Qualification : " + name + " person=" + person;
    }
}
