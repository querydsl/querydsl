package com.mysema.query.hql;

import org.junit.Test;

public class SubQueryTest extends AbstractQueryTest{
    
    protected HQLSubQuery sub(){
        return new HQLSubQuery();
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void wrongUsage(){
        sub().exists();
    }
    
    @Test
    public void test(){
        toString("exists (select cat from Cat cat)",                        sub().from(cat).exists());
        toString("exists (select cat from Cat cat where cat.weight < :a1)", sub().from(cat).where(cat.weight.lt(1)).exists());
        toString("exists (select cat from Cat cat where cat.weight < :a1)", sub().from(cat).where(cat.weight.lt(1)).unique(cat).exists());
    }
    
}
