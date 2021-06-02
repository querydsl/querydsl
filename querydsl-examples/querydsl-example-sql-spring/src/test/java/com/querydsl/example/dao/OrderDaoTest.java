package com.querydsl.example.dao;

import com.querydsl.example.dto.CustomerPaymentMethod;
import com.querydsl.example.dto.Order;
import com.querydsl.example.dto.OrderProduct;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class OrderDaoTest extends AbstractDaoTest {

    @Autowired
    OrderDao orderDao;

    @Test
    public void findAll() {
        List<Order> orders = orderDao.findAll();
        assertFalse(orders.isEmpty());
    }

    @Test
    public void findById() {
        assertNotNull(orderDao.findById(1));
    }

    @Test
    public void update() {
        Order order = orderDao.findById(1);
        orderDao.save(order);
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
        order.setOrderProducts(Collections.singleton(orderProduct));
        orderDao.save(order);
        assertNotNull(order.getId());
        orderDao.delete(order);
        assertNull(orderDao.findById(order.getId()));
    }
}
