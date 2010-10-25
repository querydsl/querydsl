package com.mysema.query.mongodb.domain;

import com.google.code.morphia.annotations.Embedded;

public final class Address {

    public Address(){
        
    }
    
    public Address(String street, String postCode, City city) {
        this.street = street; this.postCode = postCode; this.city = city;
    }
    
    public String street;
    
    public String postCode;
    
    @Embedded
    public City city;
    
}
