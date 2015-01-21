package com.querydsl.jpa.domain15;

import java.util.List;

import com.querydsl.core.annotations.QuerySupertype;

@QuerySupertype
public class Superclass<T> {

    Long id;
    
    List<T> values;
    
    List<? extends T> values2;

}
