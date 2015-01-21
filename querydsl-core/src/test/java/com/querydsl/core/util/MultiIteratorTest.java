/*
 * Copyright 2011, Mysema Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.core.util;

import static org.junit.Assert.*;

import static java.util.Arrays.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import com.mysema.commons.lang.IteratorAdapter;


public class MultiIteratorTest {
    
    private MultiIterator it;

    private List<Integer> list1 = Arrays.asList(1, 2);

    private List<Integer> list2 = Collections.emptyList();

    private List<Integer> list3, list4;

    @Test
    public void EmptyList() {
        it = new MultiIterator(Arrays.asList(list1, list2));
        while (it.hasNext()) {
            it.next();
            fail("should return false on hasNext()");
        }
    }

    @Test
    public void OneLevel() {
        it = new MultiIterator(Arrays.asList(list1));
        assertIteratorEquals(Arrays.asList(row(1), row(2)).iterator(), it);
    }

    @Test
    public void TwoLevels() {
        list2 = Arrays.asList(10, 20, 30);
        it = new MultiIterator(Arrays.asList(list1, list2));
        Iterator<Object[]> base = Arrays.asList(row(1, 10), row(1, 20),
                row(1, 30), row(2, 10), row(2, 20), row(2, 30)).iterator();
        assertIteratorEquals(base, it);
    }

    @Test
    public void ThreeLevels() {
        list1 = Arrays.asList(1, 2);
        list2 = Arrays.asList(10, 20, 30);
        list3 = Arrays.asList(100, 200, 300, 400);
        it = new MultiIterator(Arrays.asList(list1, list2, list3));
        List<Object[]> list = new ArrayList<Object[]>();
        for (Object a : row(1, 2)) {
            for (Object b : row(10, 20, 30)) {
                for (Object c : row(100, 200, 300, 400)) {
                    list.add(row(a, b, c));
                }
            }
        }
        assertIteratorEquals(list.iterator(), it);
    }

    @Test
    public void FourLevels() {
        list1 = Arrays.asList(1, 2);
        list2 = Arrays.asList(10, 20, 30);
        list3 = Arrays.asList(100, 200, 300, 400);
        list4 = Arrays.asList(1000, 2000, 3000, 4000, 5000);
        it = new MultiIterator(Arrays.asList(list1, list2, list3, list4));

        List<Object[]> list = new ArrayList<Object[]>();
        for (Object a : row(1, 2)) {
            for (Object b : row(10, 20, 30)) {
                for (Object c : row(100, 200, 300, 400)) {
                    for (Object d : row(1000, 2000, 3000, 4000, 5000)) {
                        list.add(row(a, b, c, d));
                    }
                }
            }
        }
        assertIteratorEquals(list.iterator(), it);
    }

    @Test
    public void FourLevels2() {
        list1 = new ArrayList<Integer>(100);
        for (int i = 0; i < 100; i++)
            list1.add(i + 1);
        list2 = list1;
        it = new MultiIterator(Arrays.asList(list1, list2));
        while (it.hasNext()) {
            it.next();
        }
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void test() {
        List<Integer> list1 = asList(1, 2, 3, 4);
        List<Integer> list2 = asList(10, 20, 30);
        MultiIterator<Integer> iterator = new MultiIterator<Integer>(asList(list1, list2));
        List<Object[]> list = IteratorAdapter.asList(iterator);
        
        assertEquals(asList(1, 10), asList(list.get(0)));
        assertEquals(asList(1, 20), asList(list.get(1)));
        assertEquals(asList(1, 30), asList(list.get(2)));
        assertEquals(asList(2, 10), asList(list.get(3)));
        assertEquals(asList(2, 20), asList(list.get(4)));
        assertEquals(asList(2, 30), asList(list.get(5)));
        assertEquals(asList(3, 10), asList(list.get(6)));
        assertEquals(asList(3, 20), asList(list.get(7)));
        assertEquals(asList(3, 30), asList(list.get(8)));
        assertEquals(asList(4, 10), asList(list.get(9)));
        assertEquals(asList(4, 20), asList(list.get(10)));
        assertEquals(asList(4, 30), asList(list.get(11)));
    }
    

    protected void assertIteratorEquals(Iterator<Object[]> a, Iterator<Object[]> b) {
        while (a.hasNext()) {
            assertEquals(Arrays.asList(a.next()), Arrays.asList(b.next()));
        }
        assertFalse(b.hasNext());
    }

    protected Object[] row(Object... row) {
        return row;
    }

}
