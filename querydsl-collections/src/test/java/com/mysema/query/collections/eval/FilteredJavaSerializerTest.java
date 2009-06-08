/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections.eval;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Assert;
import org.junit.Test;

import com.mysema.query.collections.ColQueryPatterns;
import com.mysema.query.collections.domain.QCat;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.Expr;

/**
 * FilteredJavaSerializerTest provides
 * 
 * @author tiwe
 * @version $Id$
 */
public class FilteredJavaSerializerTest {

    private static ColQueryPatterns ops = ColQueryPatterns.DEFAULT;

    private static QCat cat = new QCat("cat");
    private static QCat otherCat = new QCat("otherCat");
    private static QCat mate = new QCat("mate");

    @Test
    public void test1() {
        assertMatches1Expr("cat.getName().equals(a1) && true", 
                cat.name.eq("Test").and(otherCat.isNull()));
    }

    @Test
    public void test2() {
        assertMatches1Expr("cat.getName().equals(a1) && !(false)", 
                cat.name.eq("Test").and(otherCat.isNull().not()));
    }

    @Test
    public void test3() {
        assertMatches1Expr("!(cat.getName().equals(a1)) && !(false)", 
                cat.name.eq("Test").not().and(otherCat.isNull().not()));
    }

    @Test
    public void test4() {
        assertMatches1Expr("cat.getName().equals(a1) && !(false)", 
                cat.name.eq("Test").and(otherCat.isNull().not()));
    }

    @Test
    public void test5() {
        assertMatches1Expr("true && true && !(cat != null)", 
                cat.mate.eq(mate).and(otherCat.name.eq("Lucy")).and(cat.isNotNull().not()));
    }

    @Test
    public void test6() {
        assertMatches1Expr("!(false && false) && !(cat != null)", 
                cat.mate.eq(mate).and(otherCat.name.eq("Lucy")).not().and(cat.isNotNull().not()));
    }

    @Test
    public void test7() {
        assertMatches1Expr("true && true && !(cat != null)", 
                cat.mate.eq(mate).and(otherCat.name.eq("Lucy")).not().not().and(cat.isNotNull().not()));
    }

    @Test
    public void test8() {
        assertMatches1Expr("true && true", 
                cat.name.eq(otherCat.name).and(otherCat.name.matches("Bob5.*")));
    }

    @Test
    public void test9() {
        assertMatches1Expr("true", cat.name.lower().eq(otherCat.name.lower()));
    }

    @Test
    public void test10() {
        assertMatches1Expr("true && true", 
                cat.name.lower().eq(otherCat.name.lower())
                .and(otherCat.name.matches("Bob5.*")));
    }

    @Test
    public void test11() {
        assertMatches1Expr("true && true", 
                otherCat.name.lower().eq(cat.name.lower())
                .and(otherCat.name.matches("Bob5.*")));
    }

    @Test
    public void test12() {
        assertMatches2Expr("true && otherCat.getName().equals(a1)", 
                cat.name.eq("Bob").and(otherCat.name.eq("Kate")));
    }

    @Test
    public void test13() {
        assertMatches2Expr("true && otherCat.getName().equals(a2)", 
                cat.name.lower().eq("Bob").and(otherCat.name.eq("Kate")));
    }

    @Test
    public void test14() {
        assertMatches1Expr("true && true", 
                cat.name.ne(otherCat.name)
                .and(otherCat.name.matches("Kate5.*")));
        assertMatches1Expr("true && cat.getName().matches(a1)",
                otherCat.name.ne(cat.name).and(cat.name.matches("Kate5.*")));
    }

    @Test
    public void test15() {
        assertMatches1Expr("true && cat.getName().matches(a1)", 
                otherCat.name.ne(cat.name).and(cat.name.matches(".*Kate5")));
    }

    @Test
    public void test16() {
        assertMatches1Expr("cat.getName().matches(a1) && true", 
                cat.name.matches("Bob5.*").and(otherCat.name.matches("Kate5.*")));
    }

    private void assertMatches1Expr(String expected, EBoolean where) {
        JavaSerializer ser = new FilteredJavaSerializer(ops, Collections
                .<Expr<?>> singletonList(cat));
        ser.handle(where);
        Assert.assertEquals(expected, ser.toString());
    }

    private void assertMatches2Expr(String expected, EBoolean where) {
        JavaSerializer ser = new FilteredJavaSerializer(ops, Arrays
                .<Expr<?>> asList(cat, otherCat));
        ser.handle(where);
        Assert.assertEquals(expected, ser.toString());
    }

}
