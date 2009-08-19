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
    
    private static QCat cat = new QCat("cat");

    private static QCat mate = new QCat("mate");
    private static ColQueryTemplates ops = new ColQueryTemplates();
    private static QCat otherCat = new QCat("otherCat");

    @Test
    public void cat1() {
        matchCat("cat.getName().equals(a1) && true", 
                cat.name.eq("Test").and(otherCat.isNull()));
    }

    @Test
    public void cat10() {
        matchCat("true && true", 
                cat.name.lower().eq(otherCat.name.lower()).and(otherCat.name.matches("Bob5.*")));
    }

    @Test
    public void cat11() {
        matchCat("true && true", 
                otherCat.name.lower().eq(cat.name.lower())
                .and(otherCat.name.matches("Bob5.*")));
    }

    @Test
    public void cat12() {
        matchCat("true && true", 
                cat.name.ne(otherCat.name).and(otherCat.name.matches("Kate5.*")));
    }

    @Test
    public void cat13() {
        matchCat("true && cat.getName().matches(a1)",
                otherCat.name.ne(cat.name).and(cat.name.matches("Kate5.*")));
    }

    @Test
    public void cat14() {
        matchCat("true && cat.getName().matches(a1)", 
                otherCat.name.ne(cat.name).and(cat.name.matches(".*Kate5")));
    }

    @Test
    public void cat15() {
        matchCat("cat.getName().matches(a1) && true", 
                cat.name.matches("Bob5.*").and(otherCat.name.matches("Kate5.*")));
    }

    @Test
    public void cat16(){
        matchCat("true", cat.name.add("Hello").eq(otherCat.name));
    }

    @Test
    public void cat2() {
        matchCat("cat.getName().equals(a1) && !(false)", 
                cat.name.eq("Test").and(otherCat.isNull().not()));
    }

    @Test
    public void cat3() {
        matchCat("!(cat.getName().equals(a1)) && !(false)", 
                cat.name.eq("Test").not().and(otherCat.isNull().not()));
    }

    @Test
    public void cat4() {
        matchCat("cat.getName().equals(a1) && !(false)", 
                cat.name.eq("Test").and(otherCat.isNull().not()));
    }

    @Test
    public void cat5() {
        matchCat("true && true && !(cat != null)", 
                cat.mate.eq(mate).and(otherCat.name.eq("Lucy")).and(cat.isNotNull().not()));
    }

    @Test
    public void cat6() {
        matchCat("!(false && false) && !(cat != null)", 
                cat.mate.eq(mate).and(otherCat.name.eq("Lucy")).not().and(cat.isNotNull().not()));
    }

    @Test
    public void cat7() {
        matchCat("true && true && !(cat != null)", 
                cat.mate.eq(mate).and(otherCat.name.eq("Lucy")).not().not().and(cat.isNotNull().not()));
    }
    
    @Test
    public void cat8() {
        matchCat("true && true", 
                cat.name.eq(otherCat.name).and(otherCat.name.matches("Bob5.*")));
    }

    @Test
    public void cat9() {
        matchCat("true", 
                cat.name.lower().eq(otherCat.name.lower()));
    }

    @Test
    public void catAndOtherCat1() {
        matchCatAndOtherCat("true && otherCat.getName().equals(a1)", 
                cat.name.eq("Bob").and(otherCat.name.eq("Kate")));
    }

    @Test
    public void catAndOtherCat2() {
        matchCatAndOtherCat("true && otherCat.getName().equals(a1)", 
                cat.name.lower().eq("Bob").and(otherCat.name.eq("Kate")));
    }
    
    @Test
    public void catAndOtherCat3(){
        matchCatAndOtherCat("(cat.getName() + a1).equals(otherCat.getName())", 
                cat.name.add("Hello").eq(otherCat.name));
    }
    
    @Test
    public void catAndOtherCat4(){
        matchCatAndOtherCat("otherCat.getName().equals((cat.getName() + a1))", 
                otherCat.name.eq(cat.name.add("Hello")));
    }
    
    private void matchCat(String expected, EBoolean where) {
        JavaSerializer ser = new FilteredJavaSerializer(ops, Collections.<Expr<?>> singletonList(cat));
        ser.handle(where);
        Assert.assertEquals(expected, ser.toString());
    }

    private void matchCatAndOtherCat(String expected, EBoolean where) {
        JavaSerializer ser = new FilteredJavaSerializer(ops, Arrays.<Expr<?>> asList(cat, otherCat));
        ser.handle(where);
        Assert.assertEquals(expected, ser.toString());
    }

}
