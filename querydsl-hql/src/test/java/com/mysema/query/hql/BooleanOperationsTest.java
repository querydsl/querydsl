package com.mysema.query.hql;

import org.junit.Test;

public class BooleanOperationsTest extends AbstractQueryTest {
    
    @Test
    public void testBooleanOperations() {
        toString("cust is null or cat is null", cust.isNull().or(cat.isNull()));
        toString("cust is null and cat is null", cust.isNull()
                .and(cat.isNull()));
        toString("not (cust is null)", cust.isNull().not());
        cat.name.eq(cust.name.firstName).and(
                cat.bodyWeight.eq(kitten.bodyWeight));
        cat.name.eq(cust.name.firstName).or(
                cat.bodyWeight.eq(kitten.bodyWeight));
    }

    @Test
    public void testLogicalOperations() {
        // logical operations and, or, not
        toString("cat = kitten or kitten = cat", cat.eq(kitten).or(kitten.eq(cat)));
        toString("cat = kitten and kitten = cat", cat.eq(kitten).and(kitten.eq(cat)));
        toString("cat is null and (kitten is null or kitten.bodyWeight > :a1)",
                cat.isNull().and(kitten.isNull().or(kitten.bodyWeight.gt(10))));
    }
}
