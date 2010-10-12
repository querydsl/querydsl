/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.mongodb.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;

@Entity
public class User {
    
    private @Id ObjectId id;
    
    private String firstName;
    
    private String lastName;
    
    private Date created;
    
    @Embedded
    private List<Address> addresses = new ArrayList<Address>();
    
    @Embedded
    private Address mainAddress;
    
    //@Reference
    private List<User> friends = new ArrayList<User>();
    
    private int age;
    
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
    
    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
    
    public Address getMainAddress() {
        return mainAddress;
    }

    public void setMainAddress(Address mainAddress) {
        this.mainAddress = mainAddress;
    }
    
    public void setMainAddress(String street, String postCode, City city) {
        this.mainAddress = new Address(street, postCode, city);
    }

    public User addAddress(String street, String postalCode, City city) {
        addresses.add(new Address(street, postalCode, city));
        return this;
    }
    
    public List<Address> getAddresses() {
        return addresses;
    }
    
    public User addFriend(User friend) {
        friends.add(friend);
        return this;
    }

    public List<User> getFriends() {
        return friends;
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
