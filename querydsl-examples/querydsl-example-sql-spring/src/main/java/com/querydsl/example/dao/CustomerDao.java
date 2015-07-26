package com.querydsl.example.dao;

import java.util.List;

import com.mysema.query.types.Predicate;
import com.querydsl.example.dto.Customer;

public interface CustomerDao {

    Customer findById(long id);

    List<Customer> findAll(Predicate where);

    Customer save(Customer c);

    long count();

    void delete(Customer p);

}
