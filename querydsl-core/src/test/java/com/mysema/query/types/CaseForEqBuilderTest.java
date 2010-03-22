/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types;

import static com.mysema.query.alias.Alias.$;
import static com.mysema.query.alias.Alias.alias;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;


public class CaseForEqBuilderTest {
    
    public static class Customer{
        private long annualSpending;
        public long getAnnualSpending() {
            return annualSpending;
        }        
    }
    
    @Test
    public void numberTyped(){
        Customer c = alias(Customer.class, "customer");
        
        ENumber<Integer> cases = $(c.getAnnualSpending())
            .when(1000l).then(1)
            .when(2000l).then(2)
            .when(5000l).then(3)
            .otherwise(4);
        
        assertEquals(
                "case customer.annualSpending " +
                "when 1000 then 1 " +
                "when 2000 then 2 " +
                "when 5000 then 3 " +
                "else 4 " +
                "end", cases.toString());
    }
    
    @Test
    public void stringTyped(){
        Customer c = alias(Customer.class, "customer");
        
        EString cases = $(c.getAnnualSpending())
            .when(1000l).then("bronze")
            .when(2000l).then("silver")
            .when(5000l).then("gold")
            .otherwise("platinum");
        
        assertNotNull(cases);
        
    }
    
    @Test
    public void booleanTyped(){
        Customer c = alias(Customer.class, "customer");
        
        Expr<Boolean> cases = $(c.getAnnualSpending())
            .when(1000l).then(true)
            .otherwise(false);
        
        assertNotNull(cases);
        
    }
    
}
