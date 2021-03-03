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
        query = CollQueryFactory.<Cat> from(cat, Arrays.asList(cat1, cat2, cat3, cat4));
    }

    @Test
    public void avg() {
        assertEquals(3.5, query.select(cat.weight.avg()).fetchOne(), 0.0);
    }

    @Test
    public void count() {
        assertEquals(Long.valueOf(4L), query.select(cat.count()).fetchOne());
    }

    @Test
    public void countDistinct() {
        assertEquals(Long.valueOf(4L), query.select(cat.countDistinct()).fetchOne());
    }

    @Test
    public void max() {
        assertEquals(Integer.valueOf(5), query.select(cat.weight.max()).fetchOne());
    }

    @Test
    public void min() {
        assertEquals(Integer.valueOf(2), query.select(cat.weight.min()).fetchOne());
    }

    @SuppressWarnings("unchecked")
    @Test(expected = UnsupportedOperationException.class)
    public void min_and_max() {
        query.select(cat.weight.min(), cat.weight.max()).fetchOne();
    }

    @Test
    public void sum() {
        assertEquals(Integer.valueOf(14), query.select(cat.weight.sum()).fetchOne());
    }

}
