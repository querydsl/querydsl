/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql;

import org.junit.Test;

public class StringOperationsTest extends AbstractQueryTest{
    
    @Test
    public void testStringConcatenations() {
        assertToString("concat(cat.name,kitten.name)", cat.name.concat(kitten.name));
    }

    @Test
    public void testStringConversionOperations() {
        assertToString("str(cat.bodyWeight)", cat.bodyWeight.stringValue());
    }

    @Test
    public void testStringOperationsInFunctionalWay() {
        assertToString("concat(cat.name,cust.name.firstName)", cat.name.concat(cust.name.firstName));
        assertToString("lower(cat.name)", cat.name.lower());
    }


}
