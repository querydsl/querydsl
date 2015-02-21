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
package com.querydsl.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.querydsl.jpa.domain.QAccount;
import com.querydsl.jpa.domain.QInheritedProperties;
import com.querydsl.core.types.path.NumberPath;

public class FeaturesTest extends AbstractQueryTest {

    @Test
    public void DomainConstruction() {
        QInheritedProperties i = new QInheritedProperties("i");
        assertNotNull(i.superclassProperty);
        assertNotNull(i.classProperty);
    }
    
    @Test
    public void DomainConstruction2() {
        QAccount a = new QAccount("a");
        assertNotNull(a.embeddedData.someData);
    }

    @Test
    public void BasicStructure() {
        assertNull(cat.getMetadata().getParent());
    }
    
    @Test
    public void BasicStructure2() {
        assertEquals(cat, cat.alive.getMetadata().getParent());        
    }
    
    @Test
    public void BasicStructure3() {
        assertEquals("cat", cat.getMetadata().getName());
    }

    @Test
    public void ArgumentHandling() {
        // Kitty is reused, so it should be used via one named parameter
        assertToString(
                "cat.name = ?1 or cust.name.firstName = ?2 or kitten.name = ?1",
                cat.name.eq("Kitty").or(cust.name.firstName.eq("Hans")).or(kitten.name.eq("Kitty")));
    }

    @Test
    public void BasicOperations() {
        assertToString("cat.bodyWeight = kitten.bodyWeight", cat.bodyWeight.eq(kitten.bodyWeight));
    }
    
    @Test
    public void BasicOperations2() {
        assertToString("cat.bodyWeight <> kitten.bodyWeight", cat.bodyWeight.ne(kitten.bodyWeight));
    }
    
    @Test
    public void BasicOperations3() {
        assertToString(
                "cat.bodyWeight + kitten.bodyWeight = kitten.bodyWeight",
                cat.bodyWeight.add(kitten.bodyWeight).eq(kitten.bodyWeight));   
    }

    @Test
    public void EqualsAndNotEqualsForAllExpressions() {
        assertToString("cat.name = cust.name.firstName", cat.name.eq(cust.name.firstName));        
    }
    
    @Test
    public void EqualsAndNotEqualsForAllExpressions2() {
        assertToString("cat.name <> cust.name.firstName", cat.name.ne(cust.name.firstName));
    }

    @Test
    public void GroupingOperationsAndNullChecks() {
        // in, not in, between, is null, is not null, is empty, is not empty,
        // member of and not member of
        // in,
        // not in,
        // between,
        // is null,
        // is not null,
        // is empty,
        // is not empty,
        // member of
        // not member of
        kitten.in(cat.kittens);
        kitten.in(cat.kittens).not();
        kitten.bodyWeight.between(10, 20);
        kitten.bodyWeight.isNull();
        kitten.bodyWeight.isNotNull();
        cat.kittens.isEmpty();
        cat.kittens.isNotEmpty();
    }

    @Test
    public void ToString() {
        assertToString("cat", cat);
        assertToString("cat.alive", cat.alive);
        assertToString("cat.bodyWeight", cat.bodyWeight);
        assertToString("cat.name", cat.name);

        assertToString("cust.name", cust.name);
        assertToString("cust.name.firstName = ?1", cust.name.firstName.eq("Martin"));

        // toString("cat.kittens as kitten", cat.kittens.as(kitten));

        assertToString("cat.bodyWeight + ?1", cat.bodyWeight.add(10));
        assertToString("cat.bodyWeight - ?1", cat.bodyWeight.subtract(10));
        assertToString("cat.bodyWeight * ?1", cat.bodyWeight.multiply(10));
        assertToString("cat.bodyWeight / ?1", cat.bodyWeight.divide(10));

        // toString("cat.bodyWeight as bw", cat.bodyWeight.as("bw"));

        assertToString("kitten member of cat.kittens", kitten.in(cat.kittens));

        // toString("distinct cat.bodyWeight", distinct(cat.bodyWeight));
    }

//    /**
//     * specs :
//     * http://opensource.atlassian.com/projects/hibernate/browse/HHH-1538
//     */
//    @SuppressWarnings("unchecked")
//    @Test
//    public void Bug326650() {
//        assertEquals(Long.class, sum(var(Byte.class)).getType());
//        assertEquals(Long.class, sum(var(Short.class)).getType());
//        assertEquals(Long.class, sum(var(Integer.class)).getType());
//        assertEquals(Long.class, sum(var(Long.class)).getType());
//
//        assertEquals(Double.class, sum(var(Float.class)).getType());
//        assertEquals(Double.class, sum(var(Double.class)).getType());
//
//        assertEquals(BigInteger.class, sum(var(BigInteger.class)).getType());
//        assertEquals(BigDecimal.class, sum(var(BigDecimal.class)).getType());
//
//        // sum to var
//        NumberExpression<Long> sum = (NumberExpression) sum(var(Integer.class)); // via Java level cast
//        sum = sum(var(Integer.class)).longValue();
//        assertNotNull(sum);
//
//        // sum comparison
//
//        sum(var(Integer.class)).gt(0);
//        sum(var(Integer.class)).intValue().gt(0);
//
//    }

    private <D extends Number & Comparable<?>> NumberPath<D> var(Class<D> cl) {
        return new NumberPath<D>(cl, "var");
    }

}
