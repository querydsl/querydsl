package com.querydsl.example.dao;

import com.querydsl.core.types.Predicate;
import com.querydsl.example.dto.Order;

import java.util.List;

public interface OrderDao {

    Order findById(long id);
    
    List<Order> findAll(Predicate... where);
    
    Order save(Order order);
    
    long count();
        
    void delete(Order p);

}
