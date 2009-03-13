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

import com.mysema.query.collections.Domain.QCat;
import com.mysema.query.grammar.JavaOps;
import com.mysema.query.grammar.types.Expr;
import com.mysema.query.grammar.types.Expr.EBoolean;


/**
 * FilteredJavaSerializerTest provides
 *
 * @author tiwe
 * @version $Id$
 */
public class FilteredJavaSerializerTest {
    
    private static JavaOps ops = JavaOps.DEFAULT;
    
    private static QCat cat = new QCat("cat");    
    private static QCat otherCat = new QCat("otherCat");
    private static QCat mate = new QCat("mate");
    
    @Test
    public void test1(){
        assertMatches1Expr("cat.getName().equals(a1) && true",        cat.name.eq("Test").and(otherCat.isnull()));
    }   
    @Test
    public void test2(){
        assertMatches1Expr("cat.getName().equals(a1) && !(false)",    cat.name.eq("Test").and(otherCat.isnull().not()));
    }   
    @Test
    public void test3(){
        assertMatches1Expr("!(cat.getName().equals(a1)) && !(false)", cat.name.eq("Test").not().and(otherCat.isnull().not()));
    }   
    @Test
    public void test4(){
        assertMatches1Expr("cat.getName().equals(a1) && !(false)",    cat.name.eq("Test").and(otherCat.isnull().not()));
    }   
    @Test
    public void test5(){
        assertMatches1Expr("true && true && !(cat != null)",          cat.mate.eq(mate).and(otherCat.name.eq("Lucy")).and(cat.isnotnull().not()));
    }   
    @Test
    public void test6(){
        assertMatches1Expr("!(false && false) && !(cat != null)",     cat.mate.eq(mate).and(otherCat.name.eq("Lucy")).not().and(cat.isnotnull().not()));
    }   
    @Test
    public void test7(){
        assertMatches1Expr("true && true && !(cat != null)",          cat.mate.eq(mate).and(otherCat.name.eq("Lucy")).not().not().and(cat.isnotnull().not()));
    }   
    @Test
    public void test8(){        
        assertMatches1Expr("true && true",                            cat.name.eq(otherCat.name).and(otherCat.name.like("Bob5%")));
    }
    @Test
    public void test9(){        
        assertMatches1Expr("true",                                    cat.name.lower().eq(otherCat.name.lower()));
    }
    @Test
    public void test10(){        
        assertMatches1Expr("true && true",                            cat.name.lower().eq(otherCat.name.lower()).and(otherCat.name.like("Bob5%")));
    }
    @Test
    public void test11(){        
        assertMatches1Expr("true && true",                            otherCat.name.lower().eq(cat.name.lower()).and(otherCat.name.like("Bob5%")));
    }
    @Test
    public void test12(){
        assertMatches2Expr("true && otherCat.getName().equals(a1)",    cat.name.eq("Bob").and(otherCat.name.eq("Kate")));
    }
    @Test
    public void test13(){
        assertMatches2Expr("true && otherCat.getName().equals(a2)",    cat.name.lower().eq("Bob").and(otherCat.name.eq("Kate")));
    }   
    @Test
    public void test14(){
        assertMatches1Expr("true && true", cat.name.ne(otherCat.name).and(otherCat.name.like("Kate5%")));
        assertMatches1Expr("true && cat.getName().startsWith(a1, 0)", otherCat.name.ne(cat.name).and(cat.name.like("Kate5%")));
    }
    @Test
    public void test15(){
        assertMatches1Expr("true && cat.getName().endsWith(a1)", otherCat.name.ne(cat.name).and(cat.name.like("%Kate5")));
    }
        
    private void assertMatches1Expr(String expected, EBoolean where) {
        JavaSerializer ser = new FilteredJavaSerializer(ops, Collections.<Expr<?>>singletonList(cat));
        ser.handle(where);
        Assert.assertEquals(expected, ser.toString());
    }
    private void assertMatches2Expr(String expected, EBoolean where) {
        JavaSerializer ser = new FilteredJavaSerializer(ops, Arrays.<Expr<?>>asList(cat,otherCat));
        ser.handle(where);
        Assert.assertEquals(expected, ser.toString());
    }
    

}
