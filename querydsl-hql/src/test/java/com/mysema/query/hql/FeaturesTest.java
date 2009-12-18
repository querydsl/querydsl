/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql;

import static com.mysema.query.alias.Alias.$;
import static com.mysema.query.hql.HQLGrammar.sum;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.mysema.query.hql.domain.QAccount;
import com.mysema.query.hql.domain.QInheritedProperties;
import com.mysema.query.types.expr.ENumber;

/**
 * FeaturesTest provides.
 * 
 * @author tiwe
 * @version $Id$
 */
public class FeaturesTest extends AbstractQueryTest {

    @Test
    public void testDomainConstruction() {
        QInheritedProperties i = new QInheritedProperties("i");
        assertNotNull(i.superclassProperty);
        assertNotNull(i.classProperty);

        QAccount a = new QAccount("a");
        assertNotNull(a.embeddedData.someData);
    }

    @Test
    public void testBasicStructure() {
        assertNull(cat.getMetadata().getParent());
        assertEquals(cat, cat.alive.getMetadata().getParent());
        assertEquals("cat", cat.getMetadata().getExpression().toString());
    }

    @Test
    public void testArgumentHandling() {
        // Kitty is reused, so it should be used via one named parameter
        assertToString(
                "cat.name = :a1 or cust.name.firstName = :a2 or kitten.name = :a1",
                cat.name.eq("Kitty").or(cust.name.firstName.eq("Hans")).or(
                        kitten.name.eq("Kitty")));
    }

    @Test
    public void testBasicOperations() {
        assertToString("cat.bodyWeight = kitten.bodyWeight", cat.bodyWeight
                .eq(kitten.bodyWeight));
        assertToString("cat.bodyWeight != kitten.bodyWeight", cat.bodyWeight
                .ne(kitten.bodyWeight));

        assertToString(
                "cat.bodyWeight + kitten.bodyWeight = kitten.bodyWeight",
                cat.bodyWeight.add(kitten.bodyWeight).eq(kitten.bodyWeight));
    }

    @Test
    public void testEqualsAndNotEqualsForAllExpressions() {
        assertToString("cat.name = cust.name.firstName", cat.name
                .eq(cust.name.firstName));
        assertToString("cat.name != cust.name.firstName", cat.name
                .ne(cust.name.firstName));
    }

    @Test
    public void testGroupingOperationsAndNullChecks() {
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
    public void testToString() {
        assertToString("cat", cat);
        assertToString("cat.alive", cat.alive);
        assertToString("cat.bodyWeight", cat.bodyWeight);
        assertToString("cat.name", cat.name);

        assertToString("cust.name", cust.name);
        assertToString("cust.name.firstName = :a1", cust.name.firstName
                .eq("Martin"));

        // toString("cat.kittens as kitten", cat.kittens.as(kitten));

        assertToString("cat.bodyWeight + :a1", cat.bodyWeight.add(10));
        assertToString("cat.bodyWeight - :a1", cat.bodyWeight.subtract(10));
        assertToString("cat.bodyWeight * :a1", cat.bodyWeight.multiply(10));
        assertToString("cat.bodyWeight / :a1", cat.bodyWeight.divide(10));

        // toString("cat.bodyWeight as bw", cat.bodyWeight.as("bw"));

        assertToString("kitten in elements(cat.kittens)", kitten
                .in(cat.kittens));

        // toString("distinct cat.bodyWeight", distinct(cat.bodyWeight));
    }

    /**
     * specs :
     * http://opensource.atlassian.com/projects/hibernate/browse/HHH-1538
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testBug326650() {
        assertEquals(Long.class, sum($((byte) 0)).getType());
        assertEquals(Long.class, sum($((short) 0)).getType());
        assertEquals(Long.class, sum($((int) 0)).getType());
        assertEquals(Long.class, sum($((long) 0)).getType());

        assertEquals(Double.class, sum($((float) 0)).getType());
        assertEquals(Double.class, sum($((double) 0)).getType());

        assertEquals(BigInteger.class, sum($(new BigInteger("0"))).getType());
        assertEquals(BigDecimal.class, sum($(new BigDecimal("0"))).getType());

        // sum to var
        ENumber<Long> sum = (ENumber) sum($(0)); // via Java level cast
        sum = sum($(0)).longValue();
        assertNotNull(sum);

        // sum comparison

        sum($(0)).gt(0);
        sum($(0)).intValue().gt(0);

    }

}
