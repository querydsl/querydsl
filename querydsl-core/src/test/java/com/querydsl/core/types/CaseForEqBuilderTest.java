/*
 * Copyright 2011, Mysema Ltd
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
package com.querydsl.core.types;

import static com.querydsl.core.alias.Alias.$;
import static com.querydsl.core.alias.Alias.alias;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.querydsl.core.types.expr.NumberExpression;
import com.querydsl.core.types.expr.StringExpression;

public class CaseForEqBuilderTest {

    public static class Customer{
        private long annualSpending;
        public long getAnnualSpending() {
            return annualSpending;
        }
    }

    @Test
    public void NumberTyped() {
        Customer c = alias(Customer.class, "customer");

        NumberExpression<Integer> cases = $(c.getAnnualSpending())
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
    public void StringTyped() {
        Customer c = alias(Customer.class, "customer");

        StringExpression cases = $(c.getAnnualSpending())
            .when(1000l).then("bronze")
            .when(2000l).then("silver")
            .when(5000l).then("gold")
            .otherwise("platinum");

        assertNotNull(cases);

    }

    @Test
    public void BooleanTyped() {
        Customer c = alias(Customer.class, "customer");

        Expression<Boolean> cases = $(c.getAnnualSpending())
            .when(1000l).then(true)
            .otherwise(false);

        assertNotNull(cases);

    }

}
