package com.mysema.query.domain;

import static org.junit.Assert.assertEquals;

import javax.jdo.annotations.PersistenceCapable;
import javax.persistence.Entity;

import org.junit.Test;

public class KeywordsTest {

    @Entity
    public static class Order {

    }

    @Entity
    public static class From {

        String from1;
    }

    @Entity
    public static class NonKeyword {

    }

    @PersistenceCapable
    public static class Distinct {

        String distinct;

    }

    @Test
    public void test(){
        assertEquals("order1",QKeywordsTest_Order.order.toString());

        assertEquals("from1", QKeywordsTest_From.from.toString());

        assertEquals("nonKeyword",QKeywordsTest_NonKeyword.nonKeyword.toString());

        assertEquals("distinct1",QKeywordsTest_Distinct.distinct1.toString());
    }

}
