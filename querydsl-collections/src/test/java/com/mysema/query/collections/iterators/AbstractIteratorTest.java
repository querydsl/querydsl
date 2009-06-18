/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections.iterators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Arrays;
import java.util.Iterator;

/**
 * AbstractIteratorTest provides
 * 
 * @author tiwe
 * @version $Id$
 */
public abstract class AbstractIteratorTest {

    protected void assertIteratorEquals(Iterator<Object[]> a,
            Iterator<Object[]> b) {
        while (a.hasNext()) {
            assertEquals(Arrays.asList(a.next()), Arrays.asList(b.next()));
        }
        assertFalse(b.hasNext());
    }

    protected Object[] row(Object... row) {
        return row;
    }

}
