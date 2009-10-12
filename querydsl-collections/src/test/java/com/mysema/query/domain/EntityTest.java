package com.mysema.query.domain;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class EntityTest {

    @SuppressWarnings("unchecked")
    @Test
    public void test(){
        assertTrue(QEntity2.entity2 instanceof QSupertype);
        assertTrue(QEntity3.entity3 instanceof QSupertype);
    }
}
