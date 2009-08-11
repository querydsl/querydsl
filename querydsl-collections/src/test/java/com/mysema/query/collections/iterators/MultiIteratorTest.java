/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections.iterators;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

import org.junit.Test;

import com.mysema.query.alias.Alias;
import com.mysema.query.collections.IteratorSource;
import com.mysema.query.collections.impl.MultiIterator;
import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.expr.Expr;

/**
 * MultiIteratorTest provides
 * 
 * @author tiwe
 * @version $Id$
 */
public class MultiIteratorTest extends AbstractIteratorTest {

    private List<Integer> list1 = Arrays.asList(1, 2);

    private List<Integer> list2 = Collections.emptyList();

    private List<Integer> list3, list4;

    private MultiIterator it = new MultiIterator();

    private ENumber<Integer> int1 = Alias.$(1);

    private ENumber<Integer> int2 = Alias.$(2);

    private ENumber<Integer> int3 = Alias.$(3);

    private ENumber<Integer> int4 = Alias.$(4);

    private IteratorSource iteratorSource = new IteratorSource() {

        @SuppressWarnings("unchecked")
        @Nullable
        public <A> Iterator<A> getIterator(Expr<A> expr) {
            if (expr == int1)
                return (Iterator<A>) list1.iterator();
            if (expr == int2)
                return (Iterator<A>) list2.iterator();
            if (expr == int3)
                return (Iterator<A>) list3.iterator();
            if (expr == int4)
                return (Iterator<A>) list4.iterator();
            return null;
        }

        public <A> Iterator<A> getIterator(Expr<A> expr, Object[] bindings) {
            return getIterator(expr);
        }

    };

    @Test
    public void testEmptyList() {
        it.add(int1).add(int2).init(iteratorSource);
        while (it.hasNext()) {
            it.next();
            fail("should return false on hasNext()");
        }
    }

    @Test
    public void testOneLevel() {
        it.add(int1).init(iteratorSource);
        assertIteratorEquals(Arrays.asList(row(1), row(2)).iterator(), it);
    }

    @Test
    public void testTwoLevels() {
        list2 = Arrays.asList(10, 20, 30);
        it.add(int1).add(int2).init(iteratorSource);
        Iterator<Object[]> base = Arrays.asList(row(1, 10), row(1, 20),
                row(1, 30), row(2, 10), row(2, 20), row(2, 30)).iterator();
        assertIteratorEquals(base, it);
    }

    @Test
    public void testThreeLevels() {
        list1 = Arrays.asList(1, 2);
        list2 = Arrays.asList(10, 20, 30);
        list3 = Arrays.asList(100, 200, 300, 400);
        it.add(int1).add(int2).add(int3).init(iteratorSource);
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
    public void testFourLevels() {
        list1 = Arrays.asList(1, 2);
        list2 = Arrays.asList(10, 20, 30);
        list3 = Arrays.asList(100, 200, 300, 400);
        list4 = Arrays.asList(1000, 2000, 3000, 4000, 5000);
        it.add(int1).add(int2).add(int3).add(int4).init(iteratorSource);
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
    public void testFourLevels2() {
        list1 = new ArrayList<Integer>(100);
        for (int i = 0; i < 100; i++)
            list1.add(i + 1);
        list2 = list1;
        it.add(int1).add(int2);
        it.init(iteratorSource);
        // long start = System.currentTimeMillis();
        while (it.hasNext()) {
            it.next();
        }
        // long end = System.currentTimeMillis();
        // System.out.println("Iteration took " + (end-start) + " ms.");
    }

}
