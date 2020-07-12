package com.querydsl.example.dao;

import com.querydsl.example.dto.Supplier;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import javax.annotation.Resource;

public class SupplierDaoTest extends AbstractDaoTest {

    @Resource
    SupplierDao supplierDao;

    @Test
    public void findAll() {
        Flux<Supplier> setup = supplierDao.findAll();

        StepVerifier
                .create(setup)
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    public void findById() {
        Mono<Supplier> setup = supplierDao.findById(1);

        StepVerifier
                .create(setup)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void update() {
        Mono<Supplier> setup = supplierDao
                .findById(1)
                .flatMap(supplier -> supplierDao.save(supplier));

        StepVerifier
                .create(setup)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void delete() {
        Supplier supplier = new Supplier();
        Mono<Supplier> setup = supplierDao
                .save(supplier)
                .flatMap(s -> supplierDao
                        .delete(supplier)
                        .flatMap(__ -> supplierDao.findById(supplier.getId()))
                );

        StepVerifier
                .create(setup)
                .verifyComplete();
    }

}
