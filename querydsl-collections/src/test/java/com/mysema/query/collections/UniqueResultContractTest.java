package com.mysema.query.collections;

import org.junit.Test;

import com.mysema.query.NonUniqueResultException;
import com.mysema.query.types.Expression;

public class UniqueResultContractTest extends AbstractQueryTest{

    @Test(expected=NonUniqueResultException.class)
    public void Unique_Result_Throws_Exception_On_Multiple_Results(){
        MiniApi.from(cat, cats).where(cat.name.isNotNull()).uniqueResult(cat);
    }
    
    @Test
    public void UniqueResult_With_Array(){
        MiniApi.from(cat, cats).where(cat.name.isNotNull()).limit(1).uniqueResult(new Expression[]{cat});
    }
    
}
