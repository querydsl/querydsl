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
 * Convenience class that can be used in querydsl results for holding Person
 * objects.
 *
 * @version $Revision: 1.1 $
 */
@QueryEntity
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
