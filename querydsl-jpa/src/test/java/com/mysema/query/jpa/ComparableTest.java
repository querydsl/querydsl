/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jpa;

import org.junit.Test;

public class ComparableTest extends AbstractQueryTest{

    @Test
    public void Eq() {
        assertToString("cat.bodyWeight = kitten.bodyWeight",  cat.bodyWeight.eq(kitten.bodyWeight));
    }
    
    @Test
    public void Goe() {
        assertToString("cat.bodyWeight >= kitten.bodyWeight", cat.bodyWeight.goe(kitten.bodyWeight));
    }
    
    @Test
    public void Gt() {
        assertToString("cat.bodyWeight > kitten.bodyWeight",  cat.bodyWeight.gt(kitten.bodyWeight));
    }
    
    @Test
    public void Loe() {
        assertToString("cat.bodyWeight <= kitten.bodyWeight", cat.bodyWeight.loe(kitten.bodyWeight));
    }
    
    @Test
    public void Lt() {
        assertToString("cat.bodyWeight < kitten.bodyWeight",  cat.bodyWeight.lt(kitten.bodyWeight));
    }
    
    @Test
    public void Ne() {
        assertToString("cat.bodyWeight <> kitten.bodyWeight", cat.bodyWeight.ne(kitten.bodyWeight));
    }




}
