package com.querydsl.example.dao;

import com.google.common.collect.ImmutableSet;
import com.querydsl.example.dto.CustomerPaymentMethod;
import com.querydsl.example.dto.Order;
import com.querydsl.example.dto.OrderProduct;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import javax.annotation.Resource;

public class OrderDaoTest extends AbstractDaoTest {

    @Resource
    OrderDao orderDao;

    @Test
    public void findAll() {
        Flux<Order> setup = orderDao.findAll();

        StepVerifier
                .create(setup)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void findById() {
        Mono<Order> setup = orderDao.findById(1);

        StepVerifier
                .create(setup)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void update() {
        Mono<Order> setup = orderDao
                .findById(1)
                .flatMap(order -> orderDao.save(order));

        StepVerifier
                .create(setup)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void delete() {
        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setProductId(1L);
        orderProduct.setQuantity(1);

        // FIXME
        CustomerPaymentMethod paymentMethod = new CustomerPaymentMethod();

        Order order = new Order();
        order.setCustomerPaymentMethod(paymentMethod);
        order.setOrderProducts(ImmutableSet.of(orderProduct));

        Mono<Order> setup = orderDao
                .save(order)
                .flatMap(s -> orderDao
                        .delete(order)
                        .flatMap(__ -> orderDao.findById(order.getId()))
                );

        StepVerifier
                .create(setup)
                .verifyComplete();
    }
}
