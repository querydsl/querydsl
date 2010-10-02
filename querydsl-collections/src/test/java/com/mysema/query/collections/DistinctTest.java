/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.collections;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.mysema.query.types.path.NumberPath;

/**
 * DistinctTest provides
 *
 * @author tiwe
 * @version $Id$
 */
public class DistinctTest extends AbstractQueryTest {

    private NumberPath<Integer> intVar1 = new NumberPath<Integer>(Integer.class, "var1");
    private NumberPath<Integer> intVar2 = new NumberPath<Integer>(Integer.class, "var2");
    private List<Integer> list1 = Arrays.asList(1, 2, 2, 3, 3, 3, 4, 4, 4, 4);
    private List<Integer> list2 = Arrays.asList(2, 2, 3, 3, 3, 4, 4, 4, 4, 4);

    @Test
    public void SingleSource() {
        assertEquals(list1, MiniApi.from(intVar1, list1).list(intVar1));
        assertEquals(Arrays.asList(1, 2, 3, 4), MiniApi.from(intVar1, list1).listDistinct(intVar1));
        assertEquals(Arrays.asList(2, 3, 4), MiniApi.from(intVar2, list2).listDistinct(intVar2));
    }

    @Test
    public void BothSources() {
        assertEquals(100, MiniApi.from(intVar1, list1).from(intVar2, list2).list(intVar1, intVar2).size());
        assertEquals(12, MiniApi.from(intVar1, list1).from(intVar2, list2).listDistinct(intVar1, intVar2).size());
    }

    @Test
    public void CountDistinct() {
        assertEquals(10, MiniApi.from(intVar1, list1).count());
        assertEquals(4, MiniApi.from(intVar1, list1).countDistinct());
        assertEquals(3, MiniApi.from(intVar2, list2).countDistinct());
    }

}
