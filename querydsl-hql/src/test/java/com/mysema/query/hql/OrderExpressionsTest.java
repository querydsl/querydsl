/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql;

import org.junit.Test;

public class OrderExpressionsTest extends AbstractQueryTest {
    
    @Test
    public void testOrderExpressionInFunctionalWay() {
        cat.bodyWeight.asc();
        cat.bodyWeight.add(kitten.bodyWeight).asc();
    }

}
