package com.querydsl.example.dao;

import com.querydsl.core.types.Predicate;
import com.querydsl.example.dto.Person;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PersonDao {

    Mono<Person> findById(long id);

    Flux<Person> findAll(Predicate... where);

    Mono<Person> save(Person p);

    Mono<Long> count();

    Mono<Void> delete(Person p);

}
