/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import static com.mysema.query.alias.GrammarWithAlias.$;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

import com.mysema.query.collections.Domain.Cat;

/**
 * MiniApiTest provides
 * 
 * @author tiwe
 * @version $Id$
 */
public class MiniApiTest extends AbstractQueryTest {

    @Test
    public void testSimpleReject() {
        // Iterable<Integer> oneAndTwo = reject(myInts, greaterThan(2));
        Iterable<Integer> oneAndTwo = MiniApi.reject(myInts, $(0).gt(2));

        for (Integer i : oneAndTwo)
            ints.add(i);
        assertEquals(Arrays.asList(1, 2), ints);
    }

    @Test
    public void testSimpleSelect() {
        // Iterable<Integer> threeAndFour = select(myInts, greaterThan(2));
        Iterable<Integer> threeAndFour = MiniApi.select(myInts, $(0).gt(2));

        for (Integer i : threeAndFour){
            ints.add(i);   
        }            
        assertEquals(Arrays.asList(3, 4), ints);
    }

    @Test
    public void testMiniApiUsage() {
        for (Cat c : MiniApi.select(cats, cat.name.eq("Kitty"))) {
            System.out.println(c.getName());
        }
        MiniApi.select(cats, cat.kittens.size().gt(0)).iterator();
        MiniApi.select(cats, cat.mate.isNotNull()).iterator();
        MiniApi.select(cats, cat.alive.and(cat.birthdate.isNotNull())).iterator();
        MiniApi.select(cats, cat.bodyWeight.lt(cat.weight)).iterator();
        MiniApi.select(cats, cat.color.isNull().or(cat.eyecolor.eq(cat.color))).iterator();
        MiniApi.select(cats, cat.bodyWeight.between(1, 2)).iterator();

        // from where order
        MiniApi.select(cats, cat.name.eq("Kitty"), cat.name.asc()).iterator();
    }

}
