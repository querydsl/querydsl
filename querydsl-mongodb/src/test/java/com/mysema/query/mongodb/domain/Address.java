package com.mysema.query.mongodb.domain;

import com.google.code.morphia.annotations.Embedded;
import com.mysema.query.annotations.QueryEmbeddable;

@QueryEmbeddable
public final class Address {

    public Address(String street, String postCode, City city) {
        this.street = street; this.postCode = postCode; this.city = city;
    }
    
    public final String street;
    public final String postCode;
    
    @Embedded
    public final City city;
    
}
