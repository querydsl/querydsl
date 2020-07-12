package com.querydsl.example.dao;

import com.querydsl.example.dto.Customer;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import javax.annotation.Resource;

public class CustomerDaoTest extends AbstractDaoTest {

    @Resource
    CustomerDao customerDao;

    @Test
    public void findAll() {
        Flux<Customer> setup = customerDao.findAll();

        StepVerifier
                .create(setup)
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    public void findById() {
        Mono<Customer> setup = customerDao.findById(1);

        StepVerifier
                .create(setup)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void update() {
        Mono<Customer> setup = customerDao
                .findById(1)
                .flatMap(customer -> customerDao.save(customer));

        StepVerifier
                .create(setup)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void delete() {
        Mono<Customer> setup = customerDao
                .findById(1)
                .flatMap(customer -> customerDao
                        .delete(customer)
                        .flatMap(__ -> customerDao.findById(1))
                );

        StepVerifier
                .create(setup)
                .verifyComplete();
    }

}
