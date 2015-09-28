/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.core.types.dsl;

import static com.querydsl.core.alias.Alias.$;
import static com.querydsl.core.alias.Alias.alias;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.Time;

import org.junit.Test;

public class CaseForEqBuilderTest {

    public enum EnumExample { A, B }

    public static class Customer {
        private long annualSpending;
        public long getAnnualSpending() {
            return annualSpending;
        }
    }

    @Test
    public void NumberTyped() {
        Customer c = alias(Customer.class, "customer");

        NumberExpression<Integer> cases = $(c.getAnnualSpending())
            .when(1000L).then(1)
            .when(2000L).then(2)
            .when(5000L).then(3)
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
    public void StringTyped() {
        Customer c = alias(Customer.class, "customer");

        StringExpression cases = $(c.getAnnualSpending())
            .when(1000L).then("bronze")
            .when(2000L).then("silver")
            .when(5000L).then("gold")
            .otherwise("platinum");

        assertNotNull(cases);

    }

    @Test
    public void BooleanTyped() {
        Customer c = alias(Customer.class, "customer");

        BooleanExpression cases = $(c.getAnnualSpending())
            .when(1000L).then(true)
            .otherwise(false);

        assertNotNull(cases);
    }

    @Test
    public void DateType() {
        Customer c = alias(Customer.class, "customer");

        DateExpression<java.sql.Date> cases = $(c.getAnnualSpending())
                .when(1000L).then(new java.sql.Date(0))
                .otherwise(new java.sql.Date(0));

        assertNotNull(cases);
    }

    @Test
    public void DateTimeType() {
        Customer c = alias(Customer.class, "customer");

        DateTimeExpression<java.util.Date> cases = $(c.getAnnualSpending())
                .when(1000L).then(new java.util.Date(0))
                .otherwise(new java.util.Date(0));

        assertNotNull(cases);
    }

    @Test
    public void TimeType() {
        Customer c = alias(Customer.class, "customer");

        TimeExpression<Time> cases = $(c.getAnnualSpending())
                .when(1000L).then(new Time(0))
                .otherwise(new Time(0));

        assertNotNull(cases);
    }

    @Test
    public void EnumType() {
        Customer c = alias(Customer.class, "customer");

        EnumExpression<EnumExample> cases = $(c.getAnnualSpending())
                .when(1000L).then(EnumExample.A)
                .otherwise(EnumExample.B);

        assertNotNull(cases);
    }


}
