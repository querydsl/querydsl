package com.mysema.query.collections.iterators;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

/**
 * MultiIteratorTest provides
 *
 * @author tiwe
 * @version $Id$
 */
public class MultiIteratorTest {

    private MultiIterator it = new MultiIterator();
    
    @Test
    public void testOneLevel(){
        it.add(1,2).init();
        assertIteratorEquals(Arrays.asList(row(1),row(2)).iterator(), it);
    }
    
    @Test
    public void testTwoLevels() {
        it.add(1,2).add(10, 20, 30).init();
        Iterator<Object[]> base = Arrays.asList(
                row(1,10), row(1,20), row(1,30),
                row(2,10), row(2,20), row(2,30)).iterator();
        assertIteratorEquals(base, it);
    }
    
    @Test
    public void testThreeLevels() {
        it.add(1, 2).add(10, 20, 30).add(100, 200, 300, 400).init();
        List<Object[]> list = new ArrayList<Object[]>();
        for (Object a : row(1,2)){
            for (Object b : row(10, 20, 30)){
                for (Object c : row(100, 200, 300, 400)){
                    list.add(row(a,b,c));
                }
            }
        }
        assertIteratorEquals(list.iterator(), it);
    }
    
    @Test
    public void testFourLevels() {
        it.add(1, 2).add(10, 20, 30).add(100, 200, 300, 400)
            .add(1000, 2000, 3000, 4000, 5000).init();
        List<Object[]> list = new ArrayList<Object[]>();
        for (Object a : row(1,2)){
            for (Object b : row(10, 20, 30)){
                for (Object c : row(100, 200, 300, 400)){
                    for (Object d : row(1000, 2000, 3000, 4000, 5000)){
                        list.add(row(a,b,c, d));    
                    }                    
                }
            }
        }
        assertIteratorEquals(list.iterator(), it);
    }

    private void assertIteratorEquals(Iterator<Object[]> a, Iterator<Object[]> b) {
        while (a.hasNext()){
            assertArrayEquals(a.next(), b.next());
        }
        assertFalse(b.hasNext());
    }
    
    private Object[] row(Object... row){
        return row;
    }
}
