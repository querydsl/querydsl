package com.querydsl.example.dao;

import com.querydsl.core.types.Predicate;
import com.querydsl.example.dto.Customer;

import java.util.List;

public interface CustomerDao {

    Customer findById(long id);

    List<Customer> findAll(Predicate... where);

    Customer save(Customer c);

    long count();

    void delete(Customer p);

}
