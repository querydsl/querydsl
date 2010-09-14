/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jdo.models.company;

import com.mysema.query.annotations.QueryEntity;

/**
 * Phone number of a person.
 *
 * @version $Revision: 1.1 $
 */
@QueryEntity
public class PhoneNumber {
    long id; // PK when using app id
    String name;
    String number;

    public PhoneNumber() {
    }

    public PhoneNumber(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }
}
