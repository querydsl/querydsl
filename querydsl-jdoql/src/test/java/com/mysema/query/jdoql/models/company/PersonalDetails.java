/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.jdoql.models.company;

import com.mysema.query.annotations.Entity;


/**
 * Details of a person. Represents a subset of the information available from
 * Person, and so can be used as a ResultClass for SQL/JDOQL queries.
 * 
 * @version $Revision: 1.1 $
 */
@Entity
public class PersonalDetails {
    private String firstName;
    private String lastName;
    private int age;

    public PersonalDetails(){}
    
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