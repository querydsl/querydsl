package com.mysema.query.collections;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class SingleResultContractTest extends AbstractQueryTest{

    @Test
    public void SingleResult(){
        assertNotNull(MiniApi.from(cat, cats).where(cat.name.isNotNull()).singleResult(cat));
    }

}
