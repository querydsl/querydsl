package com.querydsl.example.dao;

import java.util.List;

import com.mysema.query.types.Predicate;
import com.querydsl.example.dto.Supplier;

public interface SupplierDao {

    Supplier findById(long id);

    List<Supplier> findAll(Predicate where);

    Supplier save(Supplier s);

    long count();

    void delete(Supplier s);

}
