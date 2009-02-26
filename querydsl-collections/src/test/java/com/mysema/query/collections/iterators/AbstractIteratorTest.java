package com.mysema.query.collections.iterators;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;

import java.util.Iterator;

/**
 * AbstractIteratorTest provides
 *
 * @author tiwe
 * @version $Id$
 */
public abstract class AbstractIteratorTest {
    
    protected void assertIteratorEquals(Iterator<Object[]> a, Iterator<Object[]> b) {
        while (a.hasNext()){
            assertArrayEquals(a.next(), b.next());
        }
        assertFalse(b.hasNext());
    }
    
    protected Object[] row(Object... row){
        return row;
    }

}
