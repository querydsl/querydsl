package com.querydsl.example.dao;

import java.util.List;

import com.querydsl.core.types.Predicate;
import com.querydsl.example.dto.Product;

public interface ProductDao {

    Product findById(long id);
    
    List<Product> findAll(Predicate... where);
    
    Product save(Product p);
    
    long count();
    
    void delete(Product p);
    
}
