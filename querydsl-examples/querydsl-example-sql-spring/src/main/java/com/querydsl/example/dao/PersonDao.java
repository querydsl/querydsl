package com.querydsl.example.dao;

import com.querydsl.core.types.Predicate;
import com.querydsl.example.dto.Person;

import java.util.List;

public interface PersonDao {
    
    Person findById(long id);
    
    List<Person> findAll(Predicate... where);
    
    Person save(Person p);
    
    long count();
        
    void delete(Person p);

}
