/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import static com.mysema.query.alias.Alias.$;
import static com.mysema.query.collections.MiniApi.from;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Date;

import org.junit.Test;

import com.mysema.query.animal.Cat;
import com.mysema.query.types.Expr;
import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.path.PString;

/**
 * ColQueryTest provides
 * 
 * @author tiwe
 * @version $Id$
 */
public class ColQueryTest extends AbstractQueryTest {

    @Test
    public void instanceOf() {
        assertEquals(
                Arrays.asList(c1, c2), 
                query().from(cat, Arrays.asList(c1, c2)).where(cat.instanceOf(Cat.class)).list(cat));
    }

    @Test
    public void testAfterAndBefore() {
        query().from(cat, Arrays.asList(c1, c2))
        .where(cat.birthdate.lt(new Date()),
                cat.birthdate.loe(new Date()), cat.birthdate.gt(new Date()),
                cat.birthdate.goe(new Date()))
        .list(cat);
    }

    @Test
    public void testArrayProjection() {
        // select pairs of cats with different names
        query().from(cat, cats).from(otherCat, cats).where(cat.name.ne(otherCat.name)).list(cat.name, otherCat.name);
        assertTrue(last.res.size() == 4 * 3);
    }

    @Test
    public void testCast() {
        ENumber<?> num = cat.id;
        Expr<?>[] expr = new Expr[] { num.byteValue(), num.doubleValue(),
                num.floatValue(), num.intValue(), num.longValue(),
                num.shortValue(), num.stringValue() };

        for (Expr<?> e : expr) {
            query().from(cat, Arrays.asList(c1, c2)).list(e);
        }

    }

    @Test
    public void testPrimitives() {
        // select cats with kittens
        query().from(cat, cats).where(cat.kittens.size().ne(0)).list(cat.name);
        assertTrue(last.res.size() == 4);

        // select cats without kittens
        query().from(cat, cats).where(cat.kittens.size().eq(0)).list(cat.name);
        assertTrue(last.res.size() == 0);
    }

    @Test
    public void testSimpleCases() {
        // select all cat names
        query().from(cat, cats).list(cat.name);
        assertTrue(last.res.size() == 4);

        // select all kittens
        query().from(cat, cats).list(cat.kittens);
        assertTrue(last.res.size() == 4);

        // select cats with kittens
        query().from(cat, cats).where(cat.kittens.size().gt(0)).list(cat.name);
        assertTrue(last.res.size() == 4);

        // select cats named Kitty
        query().from(cat, cats).where(cat.name.eq("Kitty")).list(cat.name);
        assertTrue(last.res.size() == 1);

        // select cats named Kitt%
        query().from(cat, cats).where(cat.name.matches("Kitt.*")).list(cat.name);
        assertTrue(last.res.size() == 1);

        query().from(cat, cats).list(
                cat.bodyWeight.add(cat.weight));
    }

    @Test
    public void testVarious() {
        PString a = new PString("a");
        PString b = new PString("b");
        for (Object[] strs : from(a, "aa", "bb", "cc")
                .from(b, Arrays.asList("a","b"))
                .where(a.startsWith(b)).list(a, b)) {
            System.out.println(Arrays.asList(strs));
        }

        query().from(cat, cats).list(cat.mate);

        query().from(cat, cats).list(cat.kittens);

        query().from(cat, cats).where(cat.kittens.isEmpty()).list(cat);

        query().from(cat, cats).where(cat.kittens.isNotEmpty()).list(cat);

        query().from(cat, cats).where(cat.name.matches("fri.*")).list($(cat.name));

    }

}
