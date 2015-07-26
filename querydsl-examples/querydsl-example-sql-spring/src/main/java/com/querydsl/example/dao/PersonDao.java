package com.querydsl.example.dao;

import java.util.List;

import com.mysema.query.types.Predicate;
import com.querydsl.example.dto.Person;

public interface PersonDao {

    Person findById(long id);

    List<Person> findAll(Predicate where);

    Person save(Person p);

    long count();

    void delete(Person p);

}
