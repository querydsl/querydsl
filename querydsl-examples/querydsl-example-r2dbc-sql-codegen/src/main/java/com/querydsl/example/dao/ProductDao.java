package com.querydsl.example.dao;

import com.querydsl.core.types.Predicate;
import com.querydsl.example.dto.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductDao {

    Mono<Product> findById(long id);

    Flux<Product> findAll(Predicate... where);

    Mono<Product> save(Product p);

    Mono<Long> count();

    Mono<Void> delete(Product p);

}
