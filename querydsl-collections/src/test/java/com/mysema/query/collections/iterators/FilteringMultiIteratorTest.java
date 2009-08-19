/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections.iterators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.mysema.query.alias.Alias;
import com.mysema.query.collections.domain.Cat;
import com.mysema.query.collections.domain.QCat;
import com.mysema.query.collections.eval.ColQueryTemplates;
import com.mysema.query.collections.impl.DefaultIndexSupport;
import com.mysema.query.collections.impl.FilteringMultiIterator;
import com.mysema.query.collections.impl.QueryIndexSupport;
import com.mysema.query.collections.impl.SimpleIteratorSource;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.expr.EString;
import com.mysema.query.types.expr.Expr;

/**
 * FilteringMultiIteratorTest provides
 * 
 * @author tiwe
 * @version $Id$
 */
public class FilteringMultiIteratorTest extends AbstractIteratorTest {

    private FilteringMultiIterator it;

    private ColQueryTemplates ops = new ColQueryTemplates();

    private Map<Expr<?>, Iterable<?>> exprToIt = new HashMap<Expr<?>, Iterable<?>>();

    private QueryIndexSupport iteratorFactory;

    private EString str1 = Alias.$("str1");
    private EString str2 = Alias.$("str2");
    private EString str3 = Alias.$("str3");

    private ENumber<Integer> int1 = Alias.$(1);
    private ENumber<Integer> int2 = Alias.$(2);
    private ENumber<Integer> int3 = Alias.$(3);
    private ENumber<Integer> int4 = Alias.$(4);

    Cat c1 = new Cat("Kitty");
    Cat c2 = new Cat("Bob");
    Cat c3 = new Cat("Alex");
    // Cat c4 = new Cat("Francis");
    QCat cat = new QCat("cat");
    QCat otherCat = new QCat("otherCat");

    @Test
    public void testSimpleCase1() {
        EBoolean where = str1.eq("one");
        it = new FilteringMultiIterator(ops, where);
        it.add(str1);
        exprToIt.put(str1, Arrays.asList("one", "two", "three"));
        iteratorFactory = new DefaultIndexSupport(new SimpleIteratorSource(
                exprToIt), ops, Arrays.asList(str1));
        it.init(iteratorFactory.getChildFor(where));

        assertIteratorEquals(Collections.singletonList(row("one")).iterator(),
                it);
    }

    @Test
    public void testSimpleCase2() {
        EBoolean where = str1.eq("one").and(str2.eq("two"));
        it = new FilteringMultiIterator(ops, where);
        it.add(str1);
        exprToIt.put(str1, Arrays.asList("one", "two", "three"));
        it.add(str2);
        exprToIt.put(str2, Arrays.asList("two", "three", "four"));
        iteratorFactory = new DefaultIndexSupport(new SimpleIteratorSource(
                exprToIt), ops, Arrays.asList(str1, str2));
        it.init(iteratorFactory.getChildFor(where));

        assertIteratorEquals(Collections.singletonList(row("one", "two"))
                .iterator(), it);
    }

    @Test
    public void testMoreComplexCases() {
        EBoolean where = str1.eq("one").and(str2.eq("two"))
                .or(str3.eq("three"));
        it = new FilteringMultiIterator(ops, where);
        it.add(str1);
        exprToIt.put(str1, Arrays.asList("one", "two", "three"));
        it.add(str2);
        exprToIt.put(str2, Arrays.asList("two", "three", "four", "five", "six",
                "seven"));
        it.add(str3);
        exprToIt.put(str3, Arrays.asList("three", "four", "five"));
        iteratorFactory = new DefaultIndexSupport(new SimpleIteratorSource(
                exprToIt), ops, Arrays.asList(str1, str2, str3));
        it.init(iteratorFactory.getChildFor(where));

        while (it.hasNext()) {
            System.out.println(Arrays.asList(it.next()));
        }
        // assertIteratorEquals(list.iterator(), it);
    }

    @Test
    public void testCats() {
        for (EBoolean where : Arrays.asList(cat.name.eq(otherCat.name),
                cat.name.eq("Kitty").and(otherCat.name.eq("Bob")), cat.name.eq(
                        "Kitty").and(otherCat.name.ne("Bob")))) {
            it = new FilteringMultiIterator(ops, where);
            it.add(cat);
            exprToIt.put(cat, Arrays.asList(c1, c2));
            it.add(otherCat);
            exprToIt.put(otherCat, Arrays.asList(c2, c3));
            // initAndDisplay(it);
            iteratorFactory = new DefaultIndexSupport(new SimpleIteratorSource(
                    exprToIt), ops, Arrays.asList(cat, otherCat));
            it.init(iteratorFactory.getChildFor(where));

            while (it.hasNext()) {
                it.next();
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testFourLevels() {
        EBoolean where = int1.eq(int2).and(int2.eq(int3)).and(int3.eq(int4));
        it = new FilteringMultiIterator(ops, where);
        List<Integer> ints = new ArrayList<Integer>(100);
        for (int i = 0; i < 100; i++)
            ints.add(i + 1);
        it.add(int1).add(int2).add(int3).add(int4);
        exprToIt.put(int1, ints);
        exprToIt.put(int2, ints);
        exprToIt.put(int3, ints);
        exprToIt.put(int4, ints);
        iteratorFactory = new DefaultIndexSupport(new SimpleIteratorSource(
                exprToIt), ops, Arrays.asList(int1, int2, int3, int4));
        it.init(iteratorFactory.getChildFor(where));
        long start = System.currentTimeMillis();
        while (it.hasNext()) {
            it.next();
        }
        long end = System.currentTimeMillis();
        System.out.println("Iteration took " + (end - start) + " ms.");
    }

}
