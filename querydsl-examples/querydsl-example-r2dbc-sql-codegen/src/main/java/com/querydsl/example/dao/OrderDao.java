package com.querydsl.example.dao;

import com.querydsl.core.types.Predicate;
import com.querydsl.example.dto.Order;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OrderDao {

    Mono<Order> findById(long id);

    Flux<Order> findAll(Predicate... where);

    Mono<Order> save(Order order);

    Mono<Long> count();

    Mono<Void> delete(Order p);

}
