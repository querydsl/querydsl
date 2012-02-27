package com.mysema.query.domain;

import com.mysema.query.annotations.QueryEntity;

@QueryEntity
public class Subclass extends com.mysema.query.domain.Superclass {
    
    private int number;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
    
    
}