package com.mysema.query.hql;

import org.junit.Test;

public class BooleanOperationsTest extends AbstractQueryTest {
    
    @Test
    public void testBooleanOperations() {
        assertToString("cust is null or cat is null", cust.isNull().or(cat.isNull()));
        assertToString("cust is null and cat is null", cust.isNull()
                .and(cat.isNull()));
        assertToString("not (cust is null)", cust.isNull().not());
        cat.name.eq(cust.name.firstName).and(
                cat.bodyWeight.eq(kitten.bodyWeight));
        cat.name.eq(cust.name.firstName).or(
                cat.bodyWeight.eq(kitten.bodyWeight));
    }

    @Test
    public void testLogicalOperations() {
        // logical operations and, or, not
        assertToString("cat = kitten or kitten = cat", cat.eq(kitten).or(kitten.eq(cat)));
        assertToString("cat = kitten and kitten = cat", cat.eq(kitten).and(kitten.eq(cat)));
        assertToString("cat is null and (kitten is null or kitten.bodyWeight > :a1)",
                cat.isNull().and(kitten.isNull().or(kitten.bodyWeight.gt(10))));
    }
}
