/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql;

import org.junit.Test;

public class AggregationTest extends AbstractQueryTest{
    
    @Test    
    public void test(){
        assertToString("max(cat.bodyWeight)", cat.bodyWeight.max());
        assertToString("min(cat.bodyWeight)", cat.bodyWeight.min());
        assertToString("avg(cat.bodyWeight)", cat.bodyWeight.avg());
        
//        assertToString("count(*)", Ops.AggOps.COUNT_ALL_AGG_EXPR);
        assertToString("count(cat)", cat.count());
        assertToString("count(distinct cat)", cat.countDistinct());
    }

}
