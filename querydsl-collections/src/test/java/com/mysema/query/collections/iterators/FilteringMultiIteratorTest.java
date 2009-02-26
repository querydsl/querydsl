/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections.iterators;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

import com.mysema.query.collections.MiniApi;
import com.mysema.query.collections.Domain.Cat;
import com.mysema.query.collections.Domain.QCat;
import com.mysema.query.grammar.Grammar;
import com.mysema.query.grammar.JavaOps;
import com.mysema.query.grammar.types.Expr.EBoolean;
import com.mysema.query.grammar.types.Expr.ENumber;
import com.mysema.query.grammar.types.Expr.EString;

/**
 * FilteringMultiIteratorTest provides
 *
 * @author tiwe
 * @version $Id$
 */
public class FilteringMultiIteratorTest extends AbstractIteratorTest{
    
    private FilteringMultiIterator it;
    
    private ENumber<Integer> int1 = MiniApi.$(0);    
    private EString str1 = MiniApi.$("str1");   
    private EString str2 = MiniApi.$("str2");
    private EString str3 = MiniApi.$("str3");
    
    Cat c1 = new Cat("Kitty");
    Cat c2 = new Cat("Bob");
    Cat c3 = new Cat("Alex");
    Cat c4 = new Cat("Francis");    
    QCat cat = new QCat("cat");    
    QCat otherCat = new QCat("otherCat");
    
    @Test
    public void testSimpleCase1(){           
        EBoolean where = str1.eq("one").and(str2.eq("two"));
        it = new FilteringMultiIterator(new JavaOps(), where);        
        it.add(str1, Arrays.asList("one","two","three"));
        it.init();
        
        assertIteratorEquals(Collections.singletonList(row("one")).iterator(), it);
    }

    @Test
    public void testSimpleCase2(){
        EBoolean where = str1.eq("one").and(str2.eq("two"));
        it = new FilteringMultiIterator(new JavaOps(), where);        
        it.add(str1, Arrays.asList("one","two","three"));
        it.add(str2, Arrays.asList("two","three","four"));
        it.init();
        
        assertIteratorEquals(Collections.singletonList(row("one","two")).iterator(), it);
    }

    @Test
    public void testMoreComplexCases(){
        EBoolean where = str1.eq("one").and(str2.eq("two")).or(str3.eq("three"));
        it = new FilteringMultiIterator(new JavaOps(), where);        
        it.add(str1, Arrays.asList("one","two","three"));
        it.add(str2, Arrays.asList("two","three","four","five","six","seven"));
        it.add(str3, Arrays.asList("three","four","five"));
        it.init();
        
        while (it.hasNext()){
            System.out.println(Arrays.asList(it.next()));
        }
//        assertIteratorEquals(list.iterator(), it);
    }
    
    @Test
    public void testCats(){
        for (EBoolean where : Arrays.asList(
                cat.name.eq(otherCat.name),
                cat.name.eq("Kitty").and(otherCat.name.eq("Bob")),
                cat.name.eq("Kitty").and(Grammar.not(otherCat.name.eq("Bob"))))){
            it = new FilteringMultiIterator(new JavaOps(), where);            
            it.add(cat, Arrays.asList(c1, c2));
            it.add(otherCat, Arrays.asList(c2, c3));        
//            initAndDisplay(it);    
            it.init();
            
            while (it.hasNext()){
                System.out.println(Arrays.asList(it.next()));
            }
            
        }
    }
    
}
