package com.querydsl.apt.domain;

import com.querydsl.core.annotations.QueryEmbeddable;
import com.querydsl.core.annotations.QueryEmbedded;

@QueryEmbeddable
public final class Address {

    public Address() {
        
    }
    
    public Address(String street, String postCode, City city) {
        this.street = street; this.postCode = postCode; this.city = city;
    }
    
    public String street;
    
    public String postCode;
    
    @QueryEmbedded
    public City city;
    
}