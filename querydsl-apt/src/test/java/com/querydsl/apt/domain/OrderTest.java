package com.querydsl.apt.domain;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.junit.Assert;
import org.junit.Test;

import com.querydsl.apt.domain.QOrderTest_Order;
import com.querydsl.apt.domain.QOrderTest_OrderItemImpl;

public class OrderTest {

    @Entity
    public static class Order {
        @OneToMany(targetEntity = OrderItemImpl.class)
        List<OrderItem> orderItems;
    }

    @Entity
    public interface OrderItem { }

    @Entity
    public static class OrderItemImpl implements OrderItem { }

    @Test
    public void test() {
        Assert.assertEquals(QOrderTest_OrderItemImpl.class,
                QOrderTest_Order.order.orderItems.any().getClass());
    }

}
