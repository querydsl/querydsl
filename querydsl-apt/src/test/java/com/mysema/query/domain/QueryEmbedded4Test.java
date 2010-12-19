package com.mysema.query.domain;

import static org.junit.Assert.*;

import org.junit.Test;

import com.mysema.query.annotations.QueryEmbedded;
import com.mysema.query.annotations.QueryEntity;

public class QueryEmbedded4Test {

    @QueryEntity
    public class User {
    
        @QueryEmbedded
        Address address;
        
    }
    
    public class Address {
        
        @QueryEmbedded
        City city;
        
        String name;
    }
    
    public class City {
     
        String name;
        
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
//        assertNotNull(QQueryEmbedded4Test_User.user.address.city.name);
    }
    
}
