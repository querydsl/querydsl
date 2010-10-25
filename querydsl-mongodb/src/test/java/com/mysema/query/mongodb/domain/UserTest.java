package com.mysema.query.mongodb.domain;

import org.junit.Test;

import com.google.code.morphia.Morphia;

public class UserTest {
    
    @Test
    public void Map(){
        City tampere = new City("Tampere", 61.30, 23.50);
        
        User user = new User();
        user.setAge(12);
        user.setFirstName("Jaakko");        
        user.addAddress("Aakatu", "00300", tampere);
        
        System.out.println(new Morphia().map(User.class).toDBObject(user));
    }

}
