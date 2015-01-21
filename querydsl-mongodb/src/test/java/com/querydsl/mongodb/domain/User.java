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
package com.querydsl.mongodb.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Reference;

@Entity
public class User extends AbstractEntity {

    public enum Gender { MALE, FEMALE }

    private String firstName;

    private String lastName;

    private Date created;

    private Gender gender;

    @Embedded
    private final List<Address> addresses = new ArrayList<Address>();

    @Embedded
    private Address mainAddress;

    @Reference
    private final List<User> friends = new ArrayList<User>();

    @Reference
    private User friend;

    @Reference
    private User enemy;

    private int age;

    public User() {
    }

    public User(String firstName, String lastName, User friend) {
        this(firstName, lastName);
        this.friend = friend;
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
        return "TestUser [id=" + getId() + ", firstName=" + firstName + ", lastName=" + lastName
                + "]";
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

    public User addAddress(Address address) {
        addresses.add(address);
        return this;
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

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public User getFriend() {
        return friend;
    }

    public void setFriend(User friend) {
        this.friend = friend;
    }

    public User getEnemy() {
        return enemy;
    }

    public void setEnemy(User enemy) {
        this.enemy = enemy;
    }

}
