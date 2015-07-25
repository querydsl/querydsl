package com.querydsl.example.dao;

import java.util.List;

import com.mysema.query.types.Predicate;
import com.querydsl.example.dto.Order;

public interface OrderDao {

    Order findById(long id);

    List<Order> findAll(Predicate where);

    Order save(Order order);

    long count();

    void delete(Order p);

}
