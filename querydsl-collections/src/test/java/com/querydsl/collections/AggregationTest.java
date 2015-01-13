package com.querydsl.collections;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class AggregationTest extends AbstractQueryTest {

    private static final QCat cat = QCat.cat;

    private CollQuery query;

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
        assertEquals(Double.valueOf(3.5), query.uniqueResult(cat.weight.avg()));
    }

    @Test
    public void Count() {
        assertEquals(Long.valueOf(4l), query.uniqueResult(cat.count()));
    }

    @Test
    public void CountDistinct() {
        assertEquals(Long.valueOf(4l), query.uniqueResult(cat.countDistinct()));
    }

    @Test
    public void Max() {
        assertEquals(Integer.valueOf(5), query.uniqueResult(cat.weight.max()));
    }

    @Test
    public void Min() {
        assertEquals(Integer.valueOf(2), query.uniqueResult(cat.weight.min()));
    }

    @Test(expected=UnsupportedOperationException.class)
    public void Min_And_Max() {
        query.uniqueResult(cat.weight.min(), cat.weight.max());
    }

    @Test
    public void Sum() {
        assertEquals(Integer.valueOf(14), query.uniqueResult(cat.weight.sum()));
    }

}
