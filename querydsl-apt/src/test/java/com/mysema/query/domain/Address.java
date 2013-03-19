package com.mysema.query.domain;

import com.mysema.query.annotations.QueryEmbeddable;
import com.mysema.query.annotations.QueryEmbedded;

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