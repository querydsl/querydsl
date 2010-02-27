/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.EBooleanConst;


/**
 * CascadingBooleanTest provides.
 * 
 * @author tiwe
 * @version $Id$
 */
public class BooleanBuilderTest {

    private EBoolean first = EBooleanConst.TRUE;
    
    private EBoolean second = EBooleanConst.FALSE;
    
    @Test
    public void test(){
//        new BooleanBuilder().and(first).or(second);
        assertEquals(first.or(second).toString(),
            new BooleanBuilder().and(first).or(second).toString());
    }
    
    @Test
    public void advanced(){
        BooleanBuilder builder = new BooleanBuilder();
        builder.andAnyOf(first, second, first);
        builder.orAllOf(first, second, first);
        System.out.println(builder);
    }
    
    @Test
    public void ifThenElse(){
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(null);
        builder.or(null);
        builder.and(second);        
        assertEquals(second, builder.getValue());
    }
    
    @Test
    public void nullSupport(){
        assertEquals(first, first.and(null));
        assertEquals(first, first.or(null));
    }
}
