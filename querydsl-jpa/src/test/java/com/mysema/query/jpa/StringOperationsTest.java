/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jpa;

import org.junit.Test;

public class StringOperationsTest extends AbstractQueryTest{

    @Test
    public void StringConcatenations() {
        assertToString("concat(cat.name,kitten.name)", cat.name.concat(kitten.name));
    }

    @Test
    public void StringConversionOperations() {
        assertToString("str(cat.bodyWeight)", cat.bodyWeight.stringValue());
    }

    @Test
    public void StringOperationsInFunctionalWay() {
        assertToString("concat(cat.name,cust.name.firstName)", cat.name.concat(cust.name.firstName));
        assertToString("lower(cat.name)", cat.name.lower());
    }

}
