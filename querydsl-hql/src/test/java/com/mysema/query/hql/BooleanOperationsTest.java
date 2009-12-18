package com.mysema.query.hql;

import org.junit.Test;

import com.mysema.query.BooleanBuilder;

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
    
    @Test
    public void booleanBuilder1(){        
        BooleanBuilder bb1 = new BooleanBuilder();
        bb1.and(cat.eq(cat));
        
        BooleanBuilder bb2 = new BooleanBuilder();
        bb2.or(cat.eq(cat));
        bb2.or(cat.eq(cat));
        
        assertToString("cat = cat and (cat = cat or cat = cat)", bb1.and(bb2));        
    }
    
    @Test
    public void booleanBuilder2(){
        BooleanBuilder bb1 = new BooleanBuilder();
        bb1.and(cat.eq(cat));
        
        BooleanBuilder bb2 = new BooleanBuilder();
        bb2.or(cat.eq(cat));
        bb2.or(cat.eq(cat));
        
        assertToString("cat = cat and (cat = cat or cat = cat)", bb1.and(bb2.getValue()));
    }
}
