package com.mysema.query.domain;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.annotations.QueryInit;

public class QueryInit2Test {
    
    @QueryEntity
    public static class Categorization{
    
        @QueryInit("account.owner")
        Event event;
    }
    
    @QueryEntity
    public static class Event{
        
        Account account;
    }
    
    @QueryEntity
    public static class Activation extends Event{
            
    }
    
    @QueryEntity
    public static class Account{
    
        Owner owner;
    }
    
    @QueryEntity
    public static class Owner{
        
    }
    
    @Test
    public void test(){
        assertNotNull(QCategorization.categorization.event.account.owner);
        assertNotNull(QCategorization.categorization.event.as(QActivation.class).account.owner);
    }

}
