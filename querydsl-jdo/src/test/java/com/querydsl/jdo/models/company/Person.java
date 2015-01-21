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
package com.querydsl.jdo.models.company;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;

import com.querydsl.core.annotations.QueryEntity;

/**
 * Person in a company.
 */
@QueryEntity
public class Person implements Cloneable {

    public static class Id implements Serializable {

        private static final long serialVersionUID = -4893934512712167318L;

        public String globalNum;

        public long personNum;

        public Id() {
        }

        public Id(String str) {
            StringTokenizer toke = new StringTokenizer(str, "::");

            str = toke.nextToken();
            this.personNum = Integer.parseInt(str);
            str = toke.nextToken();
            this.globalNum = str;
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }

            if (!(obj instanceof Id)) {
                return false;
            }

            Id c = (Id) obj;
            return personNum == c.personNum && globalNum.equals(c.globalNum);
        }

        public int hashCode() {
            return ((int) this.personNum) ^ this.globalNum.hashCode();
        }

        public String toString() {
            return String.valueOf(this.personNum) + "::"
                    + String.valueOf(this.globalNum);
        }
    }

    /** Used for the querying of static fields. */
    public static final String FIRSTNAME = "Woody";

    private static Random random = new Random();

    private int age;

    private Person bestFriend;

    private String emailAddress;

    private String firstName;

    private String globalNum; // Part of PK when app id

    private String lastName;

    private long personNum; // Part of PK when app id

    private Map<String, PhoneNumber> phoneNumbers = new HashMap<String, PhoneNumber>();

    public Person() {
    }

    public Person(long num, String first, String last, String email) {
        globalNum = "global:" + random.nextInt();
        personNum = num;
        firstName = first;
        lastName = last;
        emailAddress = email;
    }

    public String asString() {
        return "Person : number=" + getPersonNum() + " forename="
                + getFirstName() + " surname=" + getLastName() + " email="
                + getEmailAddress() + " bestfriend=" + getBestFriend();
    }

    public Object clone() {
        Object o = null;

        try {
            o = super.clone();
        } catch (CloneNotSupportedException e) {
            /* can't happen */
        }

        return o;
    }

    public boolean compareTo(Object obj) {
        // TODO Use globalNum here too ?
        Person p = (Person) obj;
        return bestFriend == p.bestFriend && firstName.equals(p.firstName)
                && lastName.equals(p.lastName)
                && emailAddress.equals(p.emailAddress)
                && personNum == p.personNum;
    }

    // Note that this is only really correct for application identity, but we
    // also use this class for datastore id
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if ((o == null) || (o.getClass() != this.getClass()))
            return false;

        Person other = (Person) o;
        return personNum == other.personNum
                && (globalNum == other.globalNum || (globalNum != null && globalNum
                        .equals(other.globalNum)));
    }

    public int getAge() {
        return age;
    }

    public Person getBestFriend() {
        return bestFriend;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getGlobalNum() {
        return globalNum;
    }

    public synchronized String getLastName() {
        return lastName;
    }

    public long getPersonNum() {
        return personNum;
    }

    public Map<String, PhoneNumber> getPhoneNumbers() {
        return phoneNumbers;
    }

    // Note that this is only really correct for application identity, but we
    // also use this class for datastore id
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (int) personNum;
        hash = 31 * hash + (null == globalNum ? 0 : globalNum.hashCode());
        return hash;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setBestFriend(Person p) {
        this.bestFriend = p;
    }

    public void setEmailAddress(String s) {
        emailAddress = s;
    }

    public void setFirstName(String s) {
        firstName = s;
    }

    public void setGlobalNum(String globalNum) {
        this.globalNum = globalNum;
    }

    public void setLastName(String s) {
        lastName = s;
    }

    public void setPersonNum(long num) {
        personNum = num;
    }
}
