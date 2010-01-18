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
    public void test_long_path(){
        assertNotNull(QQueryInit2Test_Categorization.categorization.event.account.owner);
        assertNotNull(QQueryInit2Test_Categorization.categorization.event.as(QQueryInit2Test_Activation.class).account.owner);
    }

}
