/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import static com.mysema.query.alias.GrammarWithAlias.$;
import static com.mysema.query.collections.MiniApi.from;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Date;

import org.junit.Test;

import com.mysema.query.collections.Domain.Cat;
import com.mysema.query.functions.MathFunctions;
import com.mysema.query.types.Grammar;
import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.expr.Expr;

/**
 * ColQueryTest provides
 * 
 * @author tiwe
 * @version $Id$
 */
public class ColQueryTest extends AbstractQueryTest{
     
    @Test
    public void isTypeOf(){
        assertEquals(Arrays.asList(c1, c2),
            query().from(cat, c1, c2).where(Grammar.typeOf(cat, Cat.class)).list(cat));
    }   
    
    @Test
    public void testAfterAndBefore(){
        query().from(cat, c1, c2).where(
                cat.birthdate.before(new Date()),
                cat.birthdate.boe(new Date()),
                cat.birthdate.after(new Date()),
                cat.birthdate.aoe(new Date())).list(cat);
    }    
   
    @Test
    public void testArrayProjection(){
        // select pairs of cats with different names
        query().from(cat,cats).from(otherCat,cats)
            .where(cat.name.ne(otherCat.name)).selelect(cat.name, otherCat.name);
        assertTrue(last.res.size() == 4 * 3);
    }
    
    @Test
    public void testCast(){
        ENumber<?> num = cat.id;
        Expr<?>[] expr = new Expr[]{
                num.byteValue(),  num.doubleValue(),
                num.floatValue(), num.intValue(),
                num.longValue(),  num.shortValue(),
                num.stringValue()};
        
        for (Expr<?> e : expr){
            query().from(cat, c1, c2).list(e);    
        }        
                
    }
    
    @Test
    public void testPrimitives(){
        // select cats with kittens
        query().from(cat,cats).where(cat.kittens.size().ne(0)).sel(cat.name);
        assertTrue(last.res.size() == 4);
        
        // select cats without kittens
        query().from(cat,cats).where(cat.kittens.size().eq(0)).sel(cat.name);
        assertTrue(last.res.size() == 0);
    }
    
    @Test
    public void testSimpleCases(){
        // select all cat names
        query().from(cat,cats).sel(cat.name);
        assertTrue(last.res.size() == 4);
        
        // select all kittens
        query().from(cat,cats).sel(cat.kittens);
        assertTrue(last.res.size() == 4);
        
        // select cats with kittens
        query().from(cat,cats).where(cat.kittens.size().gt(0)).sel(cat.name);
        assertTrue(last.res.size() == 4);
                
        // select cats named Kitty
        query().from(cat,cats).where(cat.name.eq("Kitty")).sel(cat.name);
        assertTrue(last.res.size() == 1);
        
        // select cats named Kitt%
        query().from(cat,cats).where(cat.name.like("Kitt%")).sel(cat.name);
        assertTrue(last.res.size() == 1);        
        
        query().from(cat,cats).sel(MathFunctions.add(cat.bodyWeight, cat.weight));        
    }
        
    @Test
    public void testVarious(){
        for(Object[] strs : from($("a"), "aa","bb","cc").from($("b"), "a","b")
                .where($("a").startsWith($("b")))
                .list($("a"),$("b"))){
            System.out.println(Arrays.asList(strs));
        }
        
        query().from(cat,cats).sel(cat.mate);
        
        query().from(cat,cats).sel(cat.kittens);
        
        query().from(cat,cats).where(cat.name.like("fri%")).sel($(cat.name));
   
    }
    
   
    

}
