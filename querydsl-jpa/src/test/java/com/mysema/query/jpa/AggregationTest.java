/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jpa;

import org.junit.Test;

public class AggregationTest extends AbstractQueryTest{

    @Test
    public void Max(){
        assertToString("max(cat.bodyWeight)", cat.bodyWeight.max());
    }
    
    @Test
    public void Min(){
        assertToString("min(cat.bodyWeight)", cat.bodyWeight.min());
    }
    
    @Test
    public void Avg(){
        assertToString("avg(cat.bodyWeight)", cat.bodyWeight.avg());
    }
    
    @Test
    public void Count(){
        assertToString("count(cat)", cat.count());
    }
    
    @Test
    public void CountDistinct(){
        assertToString("count(distinct cat)", cat.countDistinct());
    }

}
