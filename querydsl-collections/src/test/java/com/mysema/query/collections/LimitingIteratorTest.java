/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import org.apache.commons.collections15.IteratorUtils;
import org.junit.Test;

import com.mysema.query.QueryModifiers;
import com.mysema.query.collections.LimitingIterator;

public class LimitingIteratorTest {

    @Test
    public void test() {
        List<Integer> ints = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        assertEquals(Arrays.asList(1, 2), transform(ints, 2l, null));
        assertEquals(Arrays.asList(3, 4, 5), transform(ints, 3l, 2l));
        assertEquals(Arrays.asList(10), transform(ints, 10l, 9l));
    }

    private List<Integer> transform(List<Integer> ints, Long limit, @Nullable Long offset) {
        QueryModifiers modifiers = new QueryModifiers(limit, offset);
        return IteratorUtils.toList(LimitingIterator.create(ints.iterator(), modifiers));
    }

}
