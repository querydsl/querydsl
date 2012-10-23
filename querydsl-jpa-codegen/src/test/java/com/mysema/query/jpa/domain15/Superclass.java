package com.mysema.query.jpa.domain15;

import java.util.List;

import com.mysema.query.annotations.QuerySupertype;

@QuerySupertype
public class Superclass<T> {

    Long id;
    
    List<T> values;
    
    List<? extends T> values2;

}
