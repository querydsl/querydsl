/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.jdoql.models.company;

import com.mysema.query.annotations.Entity;


/**
 * Convenience class that can be used in query results for holding Person
 * objects.
 * 
 * @version $Revision: 1.1 $
 */
@Entity
public class PersonHolder {
    Person person1;
    Person person2;
    String firstName;
    String literalString;
    int literalInt;

    public PersonHolder() {
        super();
    }

    public PersonHolder(Person p1) {
        this.person1 = p1;
    }

    public PersonHolder(Person p1, Person p2, String name) {
        this.person1 = p1;
        this.person2 = p2;
        this.firstName = name;
    }

    public PersonHolder(PersonHolder holder, Person p2) {
        this.person1 = holder.getPerson1();
        this.person2 = p2;
    }

    public PersonHolder(Person p1, Person p2, String name,
            String literalString, Long literalInt) {
        this.person1 = p1;
        this.person2 = p2;
        this.firstName = name;
        this.literalString = literalString;
        this.literalInt = literalInt.intValue();
    }

    public PersonHolder(Person p1, Person p2, String name,
            String literalString, Long literalInt, Person ignoreThis) {
        this.person1 = p1;
        this.person2 = p2;
        this.firstName = name;
        this.literalString = literalString;
        this.literalInt = literalInt.intValue();
    }

    public int getLiteralInt() {
        return literalInt;
    }

    public void setLiteralInt(int literalInt) {
        this.literalInt = literalInt;
    }

    public String getLiteralString() {
        return literalString;
    }

    public void setLiteralString(String literalString) {
        this.literalString = literalString;
    }

    public Person getPerson1() {
        return person1;
    }

    public void setPerson1(Person p1) {
        this.person1 = p1;
    }

    public Person getPerson2() {
        return person2;
    }

    public void setPerson2(Person p2) {
        this.person2 = p2;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String name) {
        this.firstName = name;
    }
}