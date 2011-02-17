package com.mysema.query.collections;

import org.junit.Test;

import com.mysema.query.NonUniqueResultException;

public class UniqueResultContract extends AbstractQueryTest{

    @Test(expected=NonUniqueResultException.class)
    public void Unique_Result_Throws_Exception_On_Multiple_Results(){
        MiniApi.from(cat, cats).where(cat.name.isNotNull()).uniqueResult(cat);
    }
    
}
