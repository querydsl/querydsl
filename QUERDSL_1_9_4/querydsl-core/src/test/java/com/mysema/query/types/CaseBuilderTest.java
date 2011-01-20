/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types;

import static com.mysema.query.alias.Alias.$;
import static com.mysema.query.alias.Alias.alias;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.query.types.expr.CaseBuilder;
import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.expr.EString;

public class CaseBuilderTest {

    public static class Customer{
        private long annualSpending;
        public long getAnnualSpending() {
            return annualSpending;
        }
    }

    @Test
    public void booleanTyped(){
        Customer c = alias(Customer.class, "customer");
        Expr<Boolean> cases = new CaseBuilder()
            .when($(c.getAnnualSpending()).gt(10000)).then(true)
            .otherwise(false);

        assertEquals(
                "case " +
                "when customer.annualSpending > 10000 then true " +
                "else false " +
                "end", cases.toString());
    }

    @Test
    public void numberTyped(){
        Customer c = alias(Customer.class, "customer");
        ENumber<Integer> cases = new CaseBuilder()
            .when($(c.getAnnualSpending()).gt(10000)).then(1)
            .when($(c.getAnnualSpending()).gt(5000)).then(2)
            .when($(c.getAnnualSpending()).gt(2000)).then(3)
            .otherwise(4);

        assertEquals(
                "case " +
                "when customer.annualSpending > 10000 then 1 " +
                "when customer.annualSpending > 5000 then 2 " +
                "when customer.annualSpending > 2000 then 3 " +
                "else 4 " +
                "end", cases.toString());
    }

    @Test
    public void stringTyped(){
//        CASE
//          WHEN c.annualSpending > 10000 THEN 'Premier'
//          WHEN c.annualSpending >  5000 THEN 'Gold'
//          WHEN c.annualSpending >  2000 THEN 'Silver'
//          ELSE 'Bronze'
//        END

        Customer c = alias(Customer.class, "customer");
        EString cases = new CaseBuilder()
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

    }

}
