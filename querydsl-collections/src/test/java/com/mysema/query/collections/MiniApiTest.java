/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

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
    public void testMiniApiUsage(){        
        for (Cat c : MiniApi.select(cats, cat.name.eq("Kitty"))){
            System.out.println(c.getName());
        }
        MiniApi.select(cats, cat.kittens.size().gt(0)).iterator();
        MiniApi.select(cats, cat.mate.isnotnull()).iterator();
        MiniApi.select(cats, cat.alive.and(cat.birthdate.isnotnull())).iterator();       
        MiniApi.select(cats, cat.bodyWeight.lt(cat.weight)).iterator();
        MiniApi.select(cats, cat.color.isnull().or(cat.eyecolor.eq(cat.color))).iterator();
        MiniApi.select(cats, cat.bodyWeight.between(1, 2)).iterator();
        
        // from where order
        MiniApi.select(cats, cat.name.eq("Kitty"), cat.name.asc()).iterator();
    }
    
}
