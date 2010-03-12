package com.mysema.query.domain;

import java.io.Serializable;

import com.mysema.query.annotations.QuerySupertype;

@QuerySupertype
public class CommonIdentifiable<ID extends Serializable> extends CommonPersistence {
    
    private ID id;

    public ID getId() {
        return id;
    }
    
    
    
}