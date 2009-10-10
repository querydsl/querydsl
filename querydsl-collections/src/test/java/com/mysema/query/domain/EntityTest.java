package com.mysema.query.domain;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.mysema.query.domain.hierarchy.QEntity2;
import com.mysema.query.domain.hierarchy.QEntity3;
import com.mysema.query.domain.hierarchy.QSupertype;

public class EntityTest {

    @SuppressWarnings("unchecked")
    @Test
    public void test(){
        assertTrue(QEntity2.entity2 instanceof QSupertype);
        assertTrue(QEntity3.entity3 instanceof QSupertype);
    }
}
