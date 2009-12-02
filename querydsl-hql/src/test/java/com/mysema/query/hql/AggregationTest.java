package com.mysema.query.hql;

import org.junit.Test;

import com.mysema.query.types.expr.Expr;

public class AggregationTest extends AbstractQueryTest{
    
    @Test    
    public void test(){
        toString("max(cat.bodyWeight)", cat.bodyWeight.max());
        toString("min(cat.bodyWeight)", cat.bodyWeight.min());
        toString("avg(cat.bodyWeight)", cat.bodyWeight.avg());
        
        toString("count(*)", Expr.countAll());
        toString("count(cat)", cat.count());
        toString("count(distinct cat)", cat.countDistinct());
    }

}
