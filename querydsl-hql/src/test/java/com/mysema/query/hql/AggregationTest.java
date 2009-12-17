/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql;

import org.junit.Test;

import com.mysema.query.types.expr.Expr;

public class AggregationTest extends AbstractQueryTest{
    
    @Test    
    public void test(){
        assertToString("max(cat.bodyWeight)", cat.bodyWeight.max());
        assertToString("min(cat.bodyWeight)", cat.bodyWeight.min());
        assertToString("avg(cat.bodyWeight)", cat.bodyWeight.avg());
        
        assertToString("count(*)", Expr.countAll());
        assertToString("count(cat)", cat.count());
        assertToString("count(distinct cat)", cat.countDistinct());
    }

}
