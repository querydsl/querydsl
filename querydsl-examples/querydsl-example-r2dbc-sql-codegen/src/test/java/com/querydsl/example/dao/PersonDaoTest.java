package com.querydsl.example.dao;

import com.querydsl.example.dto.Person;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import javax.annotation.Resource;

public class PersonDaoTest extends AbstractDaoTest {

    @Resource
    PersonDao personDao;

    @Test
    public void findAll() {
        Flux<Person> setup = personDao.findAll();

        StepVerifier
                .create(setup)
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    public void findById() {
        Mono<Person> setup = personDao.findById(1);

        StepVerifier
                .create(setup)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void update() {
        Mono<Person> setup = personDao
                .findById(1)
                .flatMap(p -> personDao.save(p));

        StepVerifier
                .create(setup)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void delete() {
        Person person = new Person();
        person.setEmail("john@acme.com");

        Mono<Person> setup = personDao
                .save(person)
                .flatMap(s -> personDao
                        .delete(person)
                        .flatMap(__ -> personDao.findById(person.getId()))
                );

        StepVerifier
                .create(setup)
                .verifyComplete();
    }

}
