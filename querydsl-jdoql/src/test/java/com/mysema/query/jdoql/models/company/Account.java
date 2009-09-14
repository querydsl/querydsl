/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.jdoql.models.company;

import com.mysema.query.annotations.Entity;


/**
 * User account for a person.
 * 
 * @version $Revision: 1.2 $
 */
@Entity
public class Account {
    private long id; // PK if app id
    private String username;
    private boolean enabled;

    public Account() {
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean b) {
        enabled = b;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String s) {
        username = s;
    }
}