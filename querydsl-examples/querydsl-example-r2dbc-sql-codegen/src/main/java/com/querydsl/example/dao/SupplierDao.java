package com.querydsl.example.dao;

import com.querydsl.core.types.Predicate;
import com.querydsl.example.dto.Supplier;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SupplierDao {

    Mono<Supplier> findById(long id);

    Flux<Supplier> findAll(Predicate... where);

    Mono<Supplier> save(Supplier s);

    Mono<Long> count();

    Mono<Void> delete(Supplier s);

}
