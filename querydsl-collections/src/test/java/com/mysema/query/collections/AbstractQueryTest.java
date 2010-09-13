/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;

import com.mysema.query.alias.Alias;
import com.mysema.query.types.Expression;

/**
 * AbstractQueryTest provides
 *
 * @author tiwe
 * @version $Id$
 */
public abstract class AbstractQueryTest {

    protected Cat c1 = new Cat("Kitty");

    protected Cat c2 = new Cat("Bob");

    protected Cat c3 = new Cat("Alex");

    protected Cat c4 = new Cat("Francis");

    protected QCat cat = new QCat("cat");

    protected QCat kitten = new QCat("kitten");

    protected List<Cat> cats = Arrays.asList(c1, c2, c3, c4);

    protected List<Integer> ints = new ArrayList<Integer>();

    protected TestQuery last;

    protected QCat mate = new QCat("mate");

    protected List<Integer> myInts = new ArrayList<Integer>();

    protected QCat offspr = new QCat("offspr");

    protected QCat otherCat = new QCat("otherCat");

    @Before
    public void setUp() {
        myInts.addAll(Arrays.asList(1, 2, 3, 4));
        Alias.resetAlias();
    }

    protected List<Cat> cats(int size) {
        List<Cat> cats = new ArrayList<Cat>(size);
        for (int i = 0; i < size / 2; i++) {
            cats.add(new Cat("Kate" + (i + 1)));
            cats.add(new Cat("Bob" + (i + 1)));
        }
        return cats;
    }

    protected TestQuery query() {
        last = new TestQuery();
        return last;
    }

    static class TestQuery extends ColQueryImpl {

        List<Object> res = new ArrayList<Object>();

        @Override
        public <RT> List<RT> list(Expression<RT> projection) {
            boolean array = projection.getType().isArray();
            List<RT> rv = super.list(projection);
            for (Object o : rv) {
                System.out.println(array ? Arrays.toString((Object[])o) : o);
                res.add(o);
            }
            System.out.println();
            return rv;
        }

    }
}
