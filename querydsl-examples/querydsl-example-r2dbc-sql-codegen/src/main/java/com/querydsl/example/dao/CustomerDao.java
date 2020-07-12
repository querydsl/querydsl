package com.querydsl.example.dao;

import com.querydsl.core.types.Predicate;
import com.querydsl.example.dto.Customer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerDao {

    Mono<Customer> findById(long id);

    Flux<Customer> findAll(Predicate... where);

    Mono<Customer> save(Customer c);

    Mono<Long> count();

    Mono<Void> delete(Customer p);

}
