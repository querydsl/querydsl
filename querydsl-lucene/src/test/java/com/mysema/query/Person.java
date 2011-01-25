package com.mysema.query;

import org.joda.time.LocalDate;

import com.mysema.query.annotations.QueryEntity;

@QueryEntity
public class Person {
    private final String id;
    private final String name;
    private final LocalDate birthDate;

    public Person(String id, String name, LocalDate birthDate) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
    }

    public String getId() {
        return id;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getName() {
        return name;
    }
}


