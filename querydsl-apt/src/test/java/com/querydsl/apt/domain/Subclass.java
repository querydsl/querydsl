package com.querydsl.apt.domain;

import com.querydsl.core.annotations.QueryEntity;

@QueryEntity
public class Subclass extends com.querydsl.core.domain.Superclass {
    
    private int number;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
    
    
}