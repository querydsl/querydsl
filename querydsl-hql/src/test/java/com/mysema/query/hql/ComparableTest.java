/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql;

import org.junit.Test;

public class ComparableTest extends AbstractQueryTest{
    
    @Test
    public void testBinaryComparisonOperations() {
        // binary comparison operators =, >=, <=, <>, !=, like
        assertToString("cat.bodyWeight = kitten.bodyWeight",  cat.bodyWeight.eq(kitten.bodyWeight));
        assertToString("cat.bodyWeight >= kitten.bodyWeight", cat.bodyWeight.goe(kitten.bodyWeight));
        assertToString("cat.bodyWeight > kitten.bodyWeight",  cat.bodyWeight.gt(kitten.bodyWeight));
        assertToString("cat.bodyWeight <= kitten.bodyWeight", cat.bodyWeight.loe(kitten.bodyWeight));
        assertToString("cat.bodyWeight < kitten.bodyWeight",  cat.bodyWeight.lt(kitten.bodyWeight));
        assertToString("cat.bodyWeight <> kitten.bodyWeight", cat.bodyWeight.ne(kitten.bodyWeight));
//        toString("cat.name like :a1", cat.name.like("Kitty"));
    }

}
