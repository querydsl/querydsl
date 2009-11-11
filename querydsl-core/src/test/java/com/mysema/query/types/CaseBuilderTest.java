package com.mysema.query.types;

import static com.mysema.query.alias.Alias.$;
import static com.mysema.query.alias.Alias.alias;
import static org.junit.Assert.*;

import org.junit.Test;

import com.mysema.query.types.expr.Expr;

public class CaseBuilderTest {

    public static class Customer{
        private long annualSpending;
        public long getAnnualSpending() {
            return annualSpending;
        }        
    }
    
    @Test
    public void test(){
//        CASE                                         
//          WHEN c.annualSpending > 10000 THEN 'Premier'
//          WHEN c.annualSpending >  5000 THEN 'Gold'
//          WHEN c.annualSpending >  2000 THEN 'Silver'
//          ELSE 'Bronze'                                
//        END                                            
        
        Customer c = alias(Customer.class, "customer");
        Expr<String> cases = new CaseBuilder()
            .when($(c.getAnnualSpending()).gt(10000)).then("Premier")
            .when($(c.getAnnualSpending()).gt(5000)).then("Gold")
            .when($(c.getAnnualSpending()).gt(2000)).then("Silver")
            .otherwise("Bronze");
        
        // NOTE : this is just a test serialization, not the real one
        assertEquals(
           "case " +
           "when customer.annualSpending > 10000 then Premier " +
           "when customer.annualSpending > 5000 then Gold " +
           "when customer.annualSpending > 2000 then Silver " +
           "else Bronze " +
           "end", cases.toString());
     
        String rv =  c.getAnnualSpending() > 10000 ? "Premier" : 
                    c.getAnnualSpending() > 5000 ? "Gold" : 
                    c.getAnnualSpending() > 2000 ? "Silver" :
                    "Bronze";    
                            
    }

}
