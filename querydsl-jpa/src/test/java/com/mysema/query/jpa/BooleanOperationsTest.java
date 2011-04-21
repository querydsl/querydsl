/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jpa;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.query.BooleanBuilder;

public class BooleanOperationsTest extends AbstractQueryTest {

    @Test
    public void BooleanOperations_Or() {
        assertToString("cust is null or cat is null", cust.isNull().or(cat.isNull()));
    }
    
    @Test
    public void BooleanOperations_And() {
        assertToString("cust is null and cat is null", cust.isNull().and(cat.isNull()));
    }
    
    
    @Test
    public void BooleanOperations_Not() {
        assertToString("not (cust is null)", cust.isNull().not());
    }
    
    
    @Test
    public void BooleanOperations2_And() {
        cat.name.eq(cust.name.firstName).and(cat.bodyWeight.eq(kitten.bodyWeight));
    }
    
    @Test
    public void BooleanOperations2_Or() {
        cat.name.eq(cust.name.firstName).or(cat.bodyWeight.eq(kitten.bodyWeight));
    }

    @Test
    public void LogicalOperations_Or() {
        assertToString("cat = kitten or kitten = cat", cat.eq(kitten).or(kitten.eq(cat)));
    }
    
    @Test
    public void LogicalOperations_And() {
        assertToString("cat = kitten and kitten = cat", cat.eq(kitten).and(kitten.eq(cat)));
    }
    
    @Test
    public void LogicalOperations_And2() {
        assertToString("cat is null and (kitten is null or kitten.bodyWeight > :a1)",
                cat.isNull().and(kitten.isNull().or(kitten.bodyWeight.gt(10))));
    }

    @Test
    public void BooleanBuilder1(){
        BooleanBuilder bb1 = new BooleanBuilder();
        bb1.and(cat.eq(cat));

        BooleanBuilder bb2 = new BooleanBuilder();
        bb2.or(cat.eq(cat));
        bb2.or(cat.eq(cat));

        assertToString("cat = cat and (cat = cat or cat = cat)", bb1.and(bb2));
    }

    @Test
    public void BooleanBuilder2(){
        BooleanBuilder bb1 = new BooleanBuilder();
        bb1.and(cat.eq(cat));

        BooleanBuilder bb2 = new BooleanBuilder();
        bb2.or(cat.eq(cat));
        bb2.or(cat.eq(cat));

        assertToString("cat = cat and (cat = cat or cat = cat)", bb1.and(bb2.getValue()));
    }

    @Test
    public void BooleanBuilder_With_Null_In_Where(){
        assertEquals("from Cat cat", sub().from(cat).where(new BooleanBuilder()).toString());
    }
    
    @Test
    public void BooleanBuilder_With_Null_In_Having(){
        assertEquals("from Cat cat\ngroup by cat.name",
                sub().from(cat).groupBy(cat.name).having(new BooleanBuilder()).toString());
    }

}
