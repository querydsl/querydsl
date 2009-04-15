/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.mysema.query.grammar.types.Expr.ENumber;

/**
 * DistinctTest provides
 *
 * @author tiwe
 * @version $Id$
 */
public class DistinctTest extends AbstractQueryTest {

    @Test
    public void test(){
        ENumber<Integer> intVar1 = MiniApi.$(0);
        ENumber<Integer> intVar2 = MiniApi.$(1);
        List<Integer> list1 = Arrays.asList(1,2,2,3,3,3,4,4,4,4);
        List<Integer> list2 = Arrays.asList(2,2,3,3,3,4,4,4,4,4);

        // single source
        assertEquals(list1, MiniApi.from(intVar1, list1).list(intVar1));        
        assertEquals(Arrays.asList(1,2,3,4), MiniApi.from(intVar1, list1).listDistinct(intVar1));
        assertEquals(Arrays.asList(2,3,4), MiniApi.from(intVar2, list2).listDistinct(intVar2));
        
        // both sources
        assertEquals(100, MiniApi.from(intVar1, list1).from(intVar2, list2).list(intVar1, intVar2).size());
        assertEquals(12, MiniApi.from(intVar1, list1).from(intVar2, list2).listDistinct(intVar1, intVar2).size());
        
    }
}
