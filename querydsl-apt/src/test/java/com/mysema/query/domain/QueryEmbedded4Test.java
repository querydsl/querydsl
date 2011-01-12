package com.mysema.query.domain;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.mysema.query.annotations.QueryEmbedded;
import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.annotations.QueryInit;

public class QueryEmbedded4Test {

    @QueryEntity
    public class User {
    
        @QueryEmbedded
        @QueryInit("city.name")
        Address address;
     
        @QueryEmbedded
        Complex<String> complex;
    }
    
    public class Address {
        
        @QueryEmbedded
        City city;
        
        String name;
    }
    
    public class City {
     
        String name;
        
    }
    
    public class Complex<T extends Comparable<T>> implements Comparable<Complex<T>> {

        T a;
        
        @Override
        public int compareTo(Complex<T> arg0) {
            return 0;
        }
        
    }
    
    @Test
    public void User_Address_City(){
        assertNotNull(QQueryEmbedded4Test_User.user.address.city);
    }
    
    @Test
    public void User_Address_Name(){
        assertNotNull(QQueryEmbedded4Test_User.user.address.name);
    }
    
    @Test
    public void User_Address_City_Name(){
        assertNotNull(QQueryEmbedded4Test_User.user.address.city.name);
    }
    
    @Test
    public void User_Complex_a() {
        //FIXME Tämä testiluokka ei käänny vaan tulee virhe
        assertNotNull(QQueryEmbedded4Test_User.complex.a);
    }
    
}
