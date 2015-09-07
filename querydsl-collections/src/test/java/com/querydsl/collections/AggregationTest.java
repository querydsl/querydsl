package com.querydsl.collections;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

public class AggregationTest extends AbstractQueryTest {

    private static final QCat cat = QCat.cat;

    private CollQuery<?> query;

    @Override
    @Before
    public void setUp() {
        Cat cat1 = new Cat();
        cat1.setWeight(2);
        Cat cat2 = new Cat();
        cat2.setWeight(3);
        Cat cat3 = new Cat();
        cat3.setWeight(4);
        Cat cat4 = new Cat();
        cat4.setWeight(5);
        query = CollQueryFactory.from(cat, Arrays.asList(cat1, cat2, cat3, cat4));
    }

    @Test
    public void Avg() {
        assertEquals(3.5, query.select(cat.weight.avg()).fetchOne(), 0.0);
    }

    @Test
    public void Count() {
        assertEquals(Long.valueOf(4L), query.select(cat.count()).fetchOne());
    }

    @Test
    public void CountDistinct() {
        assertEquals(Long.valueOf(4L), query.select(cat.countDistinct()).fetchOne());
    }

    @Test
    public void Max() {
        assertEquals(Integer.valueOf(5), query.select(cat.weight.max()).fetchOne());
    }

    @Test
    public void Min() {
        assertEquals(Integer.valueOf(2), query.select(cat.weight.min()).fetchOne());
    }

    @SuppressWarnings("unchecked")
    @Test(expected = UnsupportedOperationException.class)
    public void Min_And_Max() {
        query.select(cat.weight.min(), cat.weight.max()).fetchOne();
    }

    @Test
    public void Sum() {
        assertEquals(Integer.valueOf(14), query.select(cat.weight.sum()).fetchOne());
    }

}
