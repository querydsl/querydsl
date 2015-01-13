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

import com.querydsl.core.annotations.QueryEntity;

/**
 * Details of a person. Represents a subset of the information available from
 * Person, and so can be used as a ResultClass for SQL/JDOQL queries.
 *
 * @version $Revision: 1.1 $
 */
@QueryEntity
public class PersonalDetails {
    private String firstName;
    private String lastName;
    private int age;

    public PersonalDetails() {
    }

    public PersonalDetails(String first, String last, int age) {
        firstName = first;
        lastName = last;
        this.age = age;
    }

    /**
     * Accessor for the first name.
     *
     * @return First name.
     **/
    public String getFirstName() {
        return firstName;
    }

    /**
     * Accessor for the last name.
     *
     * @return Last name.
     **/
    public String getLastName() {
        return lastName;
    }

    /**
     * Accessor for the currency of the payment.
     *
     * @return Currency of the payment.
     **/
    public int getAge() {
        return age;
    }

    /**
     * Mutator for the first name.
     *
     * @param name
     *            First name
     **/
    public void setFirstName(String name) {
        this.firstName = name;
    }

    /**
     * Mutator for the last name.
     *
     * @param name
     *            Last name
     **/
    public void setLastName(String name) {
        this.lastName = name;
    }

    /**
     * Mutator for the persons age.
     *
     * @param age
     *            age of the person
     **/
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * Utility to return the object as a string.
     *
     * @return Stringified version of this Person
     **/
    public String toString() {
        return "PersonalDetails : " + firstName + " " + lastName + " [age="
                + age + "]";
    }
}
