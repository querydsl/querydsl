package com.querydsl.example.dao;

import com.google.common.collect.ImmutableSet;
import com.querydsl.example.dto.Product;
import com.querydsl.example.dto.ProductL10n;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import javax.annotation.Resource;

public class ProductDaoTest extends AbstractDaoTest {

    @Resource
    SupplierDao supplierDao;

    @Resource
    ProductDao productDao;

    @Test
    public void findAll() {
        Flux<Product> setup = productDao.findAll();

        StepVerifier
                .create(setup)
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    public void findById() {
        Mono<Product> setup = productDao.findById(1);

        StepVerifier
                .create(setup)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void update() {
        Mono<Product> setup = productDao
                .findById(1)
                .flatMap(p -> productDao.save(p));

        StepVerifier
                .create(setup)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void delete() {
        Product product = new Product();
        product.setName("ProductX");
        product.setLocalizations(ImmutableSet.of(new ProductL10n()));

        Mono<Product> setup = supplierDao
                .findById(1)
                .flatMap(supplier -> {
                    product.setSupplier(supplier);
                    return productDao
                            .save(product)
                            .flatMap(s -> productDao
                                    .delete(product)
                                    .flatMap(__ -> productDao.findById(product.getId()))
                            );
                });

        StepVerifier
                .create(setup)
                .verifyComplete();
    }

}
