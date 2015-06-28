package com.querydsl.example.dao;

import com.querydsl.core.types.Predicate;
import com.querydsl.example.dto.Supplier;

import java.util.List;

public interface SupplierDao {

    Supplier findById(long id);

    List<Supplier> findAll(Predicate... where);

    Supplier save(Supplier s);

    long count();

    void delete(Supplier s);

}
