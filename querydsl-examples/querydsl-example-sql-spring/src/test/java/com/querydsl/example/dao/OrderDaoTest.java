package com.querydsl.example.dao;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import com.google.common.collect.ImmutableSet;
import com.querydsl.example.dto.CustomerPaymentMethod;
import com.querydsl.example.dto.Order;
import com.querydsl.example.dto.OrderProduct;

public class OrderDaoTest extends AbstractDaoTest {

    @Resource OrderDao orderDao;

    @Test
    public void FindAll() {
        List<Order> orders = orderDao.findAll(null);
        assertFalse(orders.isEmpty());
    }

    @Test
    public void FindById() {
        assertNotNull(orderDao.findById(1));
    }

    @Test
    public void Update() {
        Order order = orderDao.findById(1);
        orderDao.save(order);
    }

    @Test
    public void Delete() {
        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setProductId(1l);
        orderProduct.setQuantity(1);

        // FIXME
        CustomerPaymentMethod paymentMethod = new CustomerPaymentMethod();

        Order order = new Order();
        order.setCustomerPaymentMethod(paymentMethod);
        order.setOrderProducts(ImmutableSet.of(orderProduct));
        orderDao.save(order);
        assertNotNull(order.getId());
        orderDao.delete(order);
        assertNull(orderDao.findById(order.getId()));
    }
}
