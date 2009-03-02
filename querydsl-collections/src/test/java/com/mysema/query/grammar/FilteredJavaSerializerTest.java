/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar;

import java.util.Collections;

import org.junit.Assert;
import org.junit.Test;

import com.mysema.query.collections.Domain.QCat;
import com.mysema.query.grammar.types.Expr;
import com.mysema.query.grammar.types.Expr.EBoolean;


/**
 * FilteredJavaSerializerTest provides
 *
 * @author tiwe
 * @version $Id$
 */
public class FilteredJavaSerializerTest {
    
    private static JavaOps ops = new JavaOps();
    
    private static QCat cat = new QCat("cat");    
    private static QCat otherCat = new QCat("otherCat");
    private static QCat mate = new QCat("mate");
    
    @Test
    public void test1(){
        assertMatches("cat.getName().equals(a1) && true",        cat.name.eq("Test").and(otherCat.isnull()));
    }   
    @Test
    public void test2(){
        assertMatches("cat.getName().equals(a1) && !(false)",    cat.name.eq("Test").and(otherCat.isnull().not()));
    }   
    @Test
    public void test3(){
        assertMatches("!(cat.getName().equals(a1)) && !(false)", cat.name.eq("Test").not().and(otherCat.isnull().not()));
    }   
    @Test
    public void test4(){
        assertMatches("cat.getName().equals(a1) && !(false)",    cat.name.eq("Test").and(otherCat.isnull().not()));
    }   
    @Test
    public void test5(){
        assertMatches("true && true && !(cat != null)",          cat.mate.eq(mate).and(otherCat.name.eq("Lucy")).and(cat.isnotnull().not()));
    }   
    @Test
    public void test6(){
        assertMatches("!(false && false) && !(cat != null)",     cat.mate.eq(mate).and(otherCat.name.eq("Lucy")).not().and(cat.isnotnull().not()));
    }   
    @Test
    public void test7(){
        assertMatches("true && true && !(cat != null)",          cat.mate.eq(mate).and(otherCat.name.eq("Lucy")).not().not().and(cat.isnotnull().not()));
    }   
    @Test
    public void test8(){        
        assertMatches("true && true",                            cat.name.eq(otherCat.name).and(otherCat.name.like("Bob5%")));
    }
    @Test
    public void test9(){        
        assertMatches("true",                                    cat.name.lower().eq(otherCat.name.lower()));
    }
    @Test
    public void test10(){        
        assertMatches("true && true",                            cat.name.lower().eq(otherCat.name.lower()).and(otherCat.name.like("Bob5%")));
    }
    @Test
    public void test11(){        
        assertMatches("true && true",                            otherCat.name.lower().eq(cat.name.lower()).and(otherCat.name.like("Bob5%")));
    }
    private void assertMatches(String expected, EBoolean where) {
        JavaSerializer ser = new FilteredJavaSerializer(ops, Collections.<Expr<?>>singletonList(cat));
        ser.handle(where);
        Assert.assertEquals(expected, ser.toString());
    }
    

}
