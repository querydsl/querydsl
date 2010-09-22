/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.mongodb.domain;

import java.util.Date;

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;

@Entity
public class User {
    
    @Id ObjectId id;
    
    String firstName;
    String lastName;
    
    Date created;
    
    int age;
    
    public User() {
    }

    public User(String firstName, String lastName) {
        this.firstName = firstName; this.lastName = lastName;
        this.created = new Date();
    }
    
    public User(String firstName, String lastName, int age, Date created) {
        this.firstName = firstName; this.lastName = lastName; this.age = age; this.created = created;
    }

    @Override
    public String toString() {
        return "TestUser [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName
                + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        User other = (User) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
